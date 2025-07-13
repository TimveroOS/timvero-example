package com.timvero.example.admin.operation.pastdue;


import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.payment.ExampleCreditPayment;
import com.timvero.example.admin.product.engine.SimpleScheduledEngine;
import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.ground.util.EntityUtils;
import com.timvero.ground.util.Lang;
import com.timvero.loan.engine.mutator.Mutator;
import com.timvero.servicing.PreCalculateSynchronizer;
import com.timvero.servicing.credit.entity.Credit;
import com.timvero.servicing.credit.entity.CreditSnapshot;
import com.timvero.servicing.credit.entity.debt.Debt;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.servicing.engine.distribution.OperationRecord;
import com.timvero.servicing.engine.distribution.SnapshotRecord;
import com.timvero.servicing.engine.general.CreditOperationHandler;
import com.timvero.servicing.engine.general.Snapshot;
import com.timvero.servicing.engine.general.Snapshot.MutableDebt;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.SequencedMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.money.MonetaryAmount;
import org.springframework.core.Ordered;

public class PastDueOperationService implements CreditOperationHandler<PastDueOperation>, PreCalculateSynchronizer, Ordered {

    private final SequencedMap<String, String> SHIFT_ACCOUNTS;

    public PastDueOperationService(SequencedMap<String, String> accounts) {
        super();
        this.SHIFT_ACCOUNTS = accounts;
    }

    @Override
    public Class<PastDueOperation> getOperationType() {
        return PastDueOperation.class;
    }

    @Override
    public void synchronize(Credit credit, LocalDate today) {
        ExampleCreditCondition condition = ((ExampleCredit) EntityUtils.initializeAndUnproxy(credit)).getCondition();
        LocalDate startDate = credit.getStartDate();
        Period period = condition.getPeriod();
        List<CreditOperation> operations = credit.getOperations();
        Set<LocalDate> dates = IntStream.range(1, condition.getTerm() + 1).mapToObj(period::multipliedBy)
            .map(startDate::plus).collect(Collectors.toCollection(HashSet::new));
        LocalDate maturity = dates.stream().max(Comparator.naturalOrder()).get();
        for (CreditOperation op : operations.stream().filter(o -> o.getType().equals(PastDueOperation.TYPE)).toList()) {
            if (dates.remove(op.getDate())) {
                op.setStatus(OperationStatus.APPROVED);
            } else {
                op.setStatus(OperationStatus.CANCELED);
            }
        }

        for (LocalDate date : dates) {
            operations.add(new PastDueOperation(date, condition, maturity.equals(date)));
        }
    }

    @Override
    public void apply(PastDueOperation operation, Snapshot snapshot) {
        apply(operation, snapshot.pastRecords(), snapshot.debt);
    }

    public void apply(PastDueOperation operation, NavigableMap<LocalDate, SnapshotRecord> records, MutableDebt debt) {
        if (operation.isMaturity()) {
            SHIFT_ACCOUNTS.forEach((from, to) -> shiftAll(debt, from, to));
        } else {
            ExampleCreditCondition condition = operation.getCondition();
            if (SimpleScheduledEngine.NAME.equals(condition.getScheduleEngineName())) {
                MonetaryAmount unpaid = unpaid(operation, records);
                if (unpaid.isPositive()) {
                    for (Map.Entry<String, String> entry : SHIFT_ACCOUNTS.entrySet()) {
                        unpaid = shift(debt, entry.getKey(), entry.getValue(), unpaid);
                    }
                }
            } else {
                throw new IllegalArgumentException("Unexpected Schedule Engine Name: " + condition.getScheduleEngineName());
            }
        }
    }

    private MonetaryAmount shift(MutableDebt debt, String fromAccount, String toAccount, MonetaryAmount amount) {
        if (amount.isPositive()) {
            Optional<Mutator> from = debt.get(fromAccount);
            if (from.isPresent()) {
                MonetaryAmount shift = Lang.min(amount, from.get().get());
                if (shift.isPositive()) {
                    from.get().subtract(shift);
                    debt.getOrCreate(toAccount, amount.getCurrency()).add(shift);
                    return amount.subtract(shift);
                }
            }
        }
        return amount;
    }

    private void shiftAll(MutableDebt debt, String fromAccount, String toAccount) {
        Optional<Mutator> from = debt.get(fromAccount);
        if (from.isPresent()) {
            MonetaryAmount shift = from.get().get();
            if (shift.isPositive()) {
                from.get().subtract(shift);
                debt.getOrCreate(toAccount, shift.getCurrency()).add(shift);
            }
        }
    }

    private Debt getPaid(final NavigableMap<LocalDate, SnapshotRecord> records) {
        final LocalDate from = records.values().stream()
            .map(SnapshotRecord::operations)
            .flatMap(Collection::stream)
            .map(OperationRecord::operation)
            .filter(o -> o.getType().equals(PastDueOperation.TYPE))
            .map(CreditOperation::getDate)
            .max(Comparator.naturalOrder())
            .orElseGet(() -> records.firstKey().minusDays(1));
        return records.values().stream()
            .filter(s -> s.date().isAfter(from))
            .map(SnapshotRecord::operations)
            .flatMap(Collection::stream)
            .filter(this::isOperationPayment)
            .map(OperationRecord::finalDebt)
            .flatMap(Optional::stream)
            .reduce(Debt.ZERO, Debt::add);
    }

    private boolean isOperationPayment(OperationRecord o) {
        return ExampleCreditPayment.class.isAssignableFrom(o.operation().getClass());
    }

    public MonetaryAmount unpaid(PastDueOperation operation, NavigableMap<LocalDate, SnapshotRecord> records) {
        Debt paid = getPaid(records).negate();
        return SHIFT_ACCOUNTS.keySet().stream().map(paid::getAccount).flatMap(Optional::stream)
            .map(MonetaryAmount::negate).reduce(operation.getCondition().getRegularPayment(), MonetaryAmount::add);
    }

    public boolean isPastDue(CreditSnapshot creditSnapshot) {
        final Map<String, MonetaryAmount> accounts = creditSnapshot.getDebt().getAccounts();
        return SHIFT_ACCOUNTS.values().stream().anyMatch(accounts::containsKey);
    }

    public boolean isPastDue(SnapshotRecord creditSnapshot) {
        final Map<String, MonetaryAmount> accounts = creditSnapshot.debt().getAccounts();
        return SHIFT_ACCOUNTS.values().stream().anyMatch(accounts::containsKey);
    }

    public Optional<MonetaryAmount> getPastDueTotal(CreditSnapshot creditSnapshot) {
        return pastDue(creditSnapshot.getDebt());
    }

    private Optional<MonetaryAmount> pastDue(Debt debt) {
        return SHIFT_ACCOUNTS.values().stream().map(debt::getAccount)
            .filter(Optional::isPresent).map(Optional::get).reduce(MonetaryAmount::add);
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
