package com.timvero.example.admin.credit.tab;

import static com.timvero.example.admin.credit.CreditCalculationConfiguration.INTEREST;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.LATE_FEE;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PAST_DUE_INTEREST;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PAST_DUE_PRINCIPAL;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PRINCIPAL;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.charge.ChargeOperation;
import com.timvero.example.admin.operation.pastdue.PastDueOperation;
import com.timvero.example.admin.operation.pastdue.PastDueOperationService;
import com.timvero.example.admin.transaction.entity.BorrowerTransaction;
import com.timvero.example.admin.transaction.entity.BorrowerTransactionRepository;
import com.timvero.ground.util.MonetaryUtil;
import com.timvero.servicing.credit.CreditViewOptions;
import com.timvero.servicing.credit.entity.debt.Debt;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.engine.AccrualService;
import com.timvero.servicing.engine.CreditCalculationService;
import com.timvero.servicing.engine.distribution.OperationRecord;
import com.timvero.servicing.engine.distribution.SnapshotRecord;
import com.timvero.servicing.engine.general.Snapshot.MutableDebt;
import com.timvero.web.common.tab.EntityTabController;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

//@RequestMapping("/credit-data")
//@Controller
//@Order(1000)
public class CreditDataTab extends EntityTabController<UUID, ExampleCredit> {


    @Autowired
    private CreditViewOptions options;

    @Autowired
    private CreditCalculationService creditCalculationService;

    @Autowired
    private AccrualService accrualService;

    @Autowired
    private PastDueOperationService pastDueService;

    @Autowired
    private BorrowerTransactionRepository transactionRepository;

    @Override
    public boolean isVisible(ExampleCredit credit) {
        return credit.getCalculationDate() != null;
    }

    @Override
    protected String getTabTemplate(UUID id, Model model) throws Exception {
        ExampleCredit credit = loadEntity(id);

        LocalDate currentDate = LocalDate.now();
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("accounts", options.getAccountsOrder());
        List<CreditView> views = getViews(credit);
        model.addAttribute("views", views);
        model.addAttribute("summary",
            calculateCreditSummary(views, credit.getCondition().getPrincipal().getCurrency()));
        model.addAttribute("today", Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());

        return super.getTabTemplate(id, model);
    }

    private List<CreditView> getViews(ExampleCredit credit) {
        List<CreditView> result = new LinkedList<>();
        LocalDate today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate maturityDate = credit.getMaturityDate();
        CurrencyUnit currency = credit.getCondition().getPrincipal().getCurrency();

        NavigableMap<LocalDate, SnapshotRecord> extendedCredit = creditCalculationService.extendCredit(
            credit.getId(),
            today.isAfter(maturityDate) ? today : maturityDate);

        Map<LocalDate, List<OperationRecord>> operationsMap = getOperationsMapSortedByPastDueOperationDate(
            extendedCredit);
        MonetaryAmount zero = MonetaryUtil.zero(currency);

        if (!operationsMap.isEmpty()) {
            Iterator<Entry<LocalDate, List<OperationRecord>>> iterator = operationsMap.entrySet().stream()
                .sorted(Entry.comparingByKey())
                .iterator();

            Entry<LocalDate, List<OperationRecord>> previous = iterator.next();
            Entry<LocalDate, List<OperationRecord>> current = iterator.next();
            Entry<LocalDate, List<OperationRecord>> next = iterator.hasNext() ? iterator.next() : null;

            Debt balance = Debt.ZERO;

            for (OperationRecord record : previous.getValue().stream()
                .filter(o -> ChargeOperation.class.isAssignableFrom(o.operation().getClass()))
                .sorted(Comparator.comparing(o -> o.operation().getId()))
                .toList()) {
                ChargeOperation operation = (ChargeOperation) record.operation();
                balance = operation.getFinalDebt().orElse(Debt.ZERO);
            }

            do {
                LocalDate from = previous.getKey();
                LocalDate to = current.getKey();

                boolean isPeriodContainsToday = from.isBefore(today) && !to.isBefore(today);

                NavigableMap<LocalDate, SnapshotRecord> subExtendedCredit = extendedCredit.subMap(from, false, to,
                    true);

                SnapshotRecord currentSnapshot = subExtendedCredit.get(to);

                MonetaryAmount overpaymentApplied = MonetaryUtil.zero(currency);

                List<PaymentView> paymentViews = new LinkedList<>();

                Collection<OperationRecord> operations = current.getValue();

                int paymentOrder = 0;
                MonetaryAmount bankAmount = MonetaryUtil.zero(currency);

                for (OperationRecord o : operations.stream()
                    .filter(o -> CreditPayment.class.isAssignableFrom(o.operation().getClass()))
                    .sorted(Comparator.comparing(o -> o.operation().getId()))
                    .toList()) {
                    CreditPayment payment = (CreditPayment) o.operation();
                    Collection<BorrowerTransaction> transactions = transactionRepository.findAllByOperation(
                        payment);
                    Optional<Debt> debtOpt = o.finalDebt();
                    if (debtOpt.isPresent() && !payment.getDate().isAfter(today)) {
                        paymentViews.add(
                            new PaymentView(payment.getAmount(), payment.getDate(), paymentOrder++,
                                false));
                        bankAmount = bankAmount.add(payment.getAmount());
                    }
                }

                Debt operationsDebt = Debt.ZERO;
                for (OperationRecord o : operations) {
                    if (PastDueOperation.class.isAssignableFrom(o.operation().getClass())) {
                        if (o.finalDebt().isPresent()) {
                            Debt debt = o.finalDebt().get();
                            MonetaryAmount principal = debt.getAccount(PAST_DUE_PRINCIPAL).orElse(zero);
                            MonetaryAmount interest = debt.getAccount(PAST_DUE_INTEREST).orElse(zero);

                            MutableDebt tmpDebt = new MutableDebt();

                            tmpDebt.getOrCreate(PRINCIPAL, currency).set(principal);
                            tmpDebt.getOrCreate(INTEREST, currency).set(interest);

                            operationsDebt = operationsDebt.add(tmpDebt.toFinal());
                            tmpDebt = new MutableDebt();
                            tmpDebt.getOrCreate(PRINCIPAL, currency).set(principal);
                            balance = balance.subtract(tmpDebt.toFinal());
                        }
                    }
                }

                if (operationsDebt != Debt.ZERO) {

                    MutableDebt currentDebt = new MutableDebt();

                    currentDebt.getOrCreate(PAST_DUE_PRINCIPAL, currency)
                        .set(currentSnapshot.debt().getAccount(PAST_DUE_PRINCIPAL).orElse(zero));
                    currentDebt.getOrCreate(PAST_DUE_INTEREST, currency)
                        .set(currentSnapshot.debt().getAccount(PAST_DUE_INTEREST).orElse(zero));
                    currentDebt.getOrCreate(LATE_FEE, currency)
                        .set(currentSnapshot.debt().getAccount(LATE_FEE).orElse(zero));
                    Debt toBePaid = currentDebt.toFinal();

                    MonetaryAmount total = operationsDebt.getTotal().orElse(zero);
                    MonetaryAmount difference = MonetaryUtil.zero(currency);
                    if (from.isBefore(today)) {
                        difference = bankAmount.subtract(total);
                    }

                    result.add(
                        new CreditView(to, operationsDebt.getTotal().orElse(zero),
                            operationsDebt.getAccount(PRINCIPAL).orElse(zero),
                            operationsDebt.getAccount(INTEREST).orElse(zero),
                            balance.getTotal().orElse(zero), bankAmount, difference,
                            isPeriodContainsToday ? toBePaid : null, paymentViews)
                    );
                }

                previous = current;
                current = next;
                next = iterator.hasNext() ? iterator.next() : null;

            } while (current != null);


        }
        return result;
    }

    private Map<LocalDate, List<OperationRecord>> getOperationsMapSortedByPastDueOperationDate(
        NavigableMap<LocalDate, SnapshotRecord> extendedCredit) {

        Map<LocalDate, List<OperationRecord>> result = new TreeMap<>();

        List<LocalDate> pastDueOrMonitoringFeeOperationsDates = new LinkedList<>();

        LocalDate chargeOperationDate = extendedCredit.values().stream()
            .map(SnapshotRecord::operations)
            .flatMap(Collection::stream)
            .filter(o -> ChargeOperation.class.isAssignableFrom(o.operation().getClass()))
            .findFirst().map(OperationRecord::date)
            .orElseThrow(() -> new IllegalStateException("No charge operation found"));

        pastDueOrMonitoringFeeOperationsDates.add(chargeOperationDate);

        extendedCredit.values().stream()
            .map(SnapshotRecord::operations)
            .flatMap(Collection::stream)
            .filter(o -> PastDueOperation.class.isAssignableFrom(o.operation().getClass()))
            .forEach(operationRecord -> {
                LocalDate date = operationRecord.date();
                pastDueOrMonitoringFeeOperationsDates.add(date);
            });

        Iterator<LocalDate> iterator = pastDueOrMonitoringFeeOperationsDates.iterator();

        LocalDate to = iterator.next();

        LocalDate from = to.minusDays(1);
        do {
            LocalDate finalFrom = from;
            LocalDate finalTo = to;
            extendedCredit.values().stream()
                .map(SnapshotRecord::operations)
                .flatMap(Collection::stream)
                .filter(o -> o.date().isAfter(finalFrom) && !o.date().isAfter(finalTo))
                .forEach(operationRecord -> {
                    result.computeIfAbsent(finalTo, k -> new ArrayList<>()).add(operationRecord);
                });

            from = to;
            to = iterator.hasNext() ? iterator.next() : null;
        } while (to != null);

        return result;
    }

    public MonetaryAmount total(Debt debt) {
        return debt.getAccounts().values().stream().reduce(MonetaryAmount::add).orElse(null);
    }

    private CreditSummary calculateCreditSummary(List<CreditView> creditViews, CurrencyUnit currency) {
        if (creditViews == null || creditViews.isEmpty()) {
            throw new IllegalArgumentException("Credit views list cannot be null or empty");
        }

        MonetaryAmount zero = MonetaryUtil.zero(currency);

        MonetaryAmount totalAmount = zero;
        MonetaryAmount totalPrincipal = zero;
        MonetaryAmount totalInterest = zero;
        MonetaryAmount totalMonitoringFee = zero;
        MonetaryAmount totalBankAmount = zero;
        MonetaryAmount totalDifference = zero;

        for (CreditView creditView : creditViews) {
            if (creditView.amount() != null) {
                totalAmount = totalAmount.add(creditView.amount());
            }
            if (creditView.principal() != null) {
                totalPrincipal = totalPrincipal.add(creditView.principal());
            }
            if (creditView.interest() != null) {
                totalInterest = totalInterest.add(creditView.interest());
            }
            if (creditView.bankAmount() != null) {
                totalBankAmount = totalBankAmount.add(creditView.bankAmount());
            }
            if (creditView.difference() != null) {
                totalDifference = totalDifference.add(creditView.difference());
            }
        }

        return new CreditSummary(totalAmount, totalPrincipal, totalInterest,
            totalMonitoringFee, totalBankAmount, totalDifference);
    }


    public record CreditView(LocalDate date, MonetaryAmount amount, MonetaryAmount principal,
                             MonetaryAmount interest, MonetaryAmount balance,
                             MonetaryAmount bankAmount, MonetaryAmount difference,
                             Debt toBePaid, List<PaymentView> payments) {

    }

    public record PaymentView(MonetaryAmount amount, LocalDate actualDate, Integer order,
                              boolean early) {

    }

    public record CreditSummary(MonetaryAmount amount, MonetaryAmount principal,
                                MonetaryAmount interest, MonetaryAmount monitoringFee,
                                MonetaryAmount bankAmount, MonetaryAmount difference) {

    }


    public enum PaymentStatus {
        PAID, DELAY, OVERDUE, PENDING
    }

}
