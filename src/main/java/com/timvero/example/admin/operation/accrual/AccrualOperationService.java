package com.timvero.example.admin.operation.accrual;

import static com.timvero.servicing.credit.entity.operation.OperationStatus.APPROVED;

import com.timvero.example.admin.operation.pastdue.PastDueOperation;
import com.timvero.servicing.PreCalculateSynchronizer;
import com.timvero.servicing.credit.entity.Credit;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.servicing.engine.general.CreditOperationHandler;
import com.timvero.servicing.engine.general.Snapshot;
import com.timvero.servicing.engine.general.Snapshot.MutableDebt;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.Ordered;

public class AccrualOperationService implements CreditOperationHandler<AccrualOperation>, PreCalculateSynchronizer, Ordered {

    private static final Set<Integer> CAUSE_OPERATIONS =
        Set.of(CreditPayment.TYPE, PastDueOperation.TYPE);

    public AccrualOperationService() {
        super();
    }

    @Override
    public Class<AccrualOperation> getOperationType() {
        return AccrualOperation.class;
    }

    @Override
    public void synchronize(Credit credit, LocalDate today) {
        List<CreditOperation> operations = credit.getOperations();
        Set<LocalDate> dates = operations.stream()
            .filter(o -> CAUSE_OPERATIONS.contains(o.getType()))
            .filter(o -> o.getStatus() == APPROVED)
            .map(CreditOperation::getDate).collect(Collectors.toCollection(HashSet::new));
        for (CreditOperation op : operations.stream().filter(o -> o.getType().equals(AccrualOperation.TYPE)).toList()) {
            if (dates.remove(op.getDate())) {
                op.setStatus(OperationStatus.APPROVED);
            } else {
                op.setStatus(OperationStatus.CANCELED);
            }
        }

        for (LocalDate date : dates) {
            operations.add(new AccrualOperation(date));
        }
    }

    @Override
    public void apply(AccrualOperation operation, Snapshot snapshot) {
        MutableDebt accurals = snapshot.calculateAccurals();
        accurals.toFinal().getAccounts().forEach((account, amount) -> {
            accurals.getOrCreate(account, amount.getCurrency()).subtract(amount);
            snapshot.debt.getOrCreate(account, amount.getCurrency()).add(amount);
        });
    }

    @Override
    public int getOrder() {
        return 2000;
    }
}
