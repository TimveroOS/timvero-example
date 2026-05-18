package com.timvero.example.admin.operation.accrual;

import static com.timvero.example.admin.credit.CreditCalculationConfiguration.INTEREST;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PRINCIPAL;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.scheduled.day_count.DayCountMethod;
import com.timvero.scheduled.day_count.DayCounter;
import com.timvero.scheduled.day_count.PaymentGrid;
import com.timvero.servicing.engine.accural.RangeBasedAccrualEngine;
import com.timvero.servicing.engine.distribution.OperationRecord;
import com.timvero.servicing.engine.distribution.SnapshotRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;

public class InterestAccrualEngine extends RangeBasedAccrualEngine<ExampleCredit> {

    @Autowired
    private Map<String, DayCountMethod> dayCountMethods;

    @Override
    public String getAccrualAccount() {
        return INTEREST;
    }

    @Override
    protected NavigableMap<LocalDate, MonetaryAmount> getBasisRanges(ExampleCredit credit,
        Collection<OperationRecord> operations, NavigableMap<LocalDate, SnapshotRecord> snapshots,
        LocalDate date) {
        TreeMap<LocalDate, MonetaryAmount> result = new TreeMap<>();
        Optional<MonetaryAmount> current, previous = Optional.empty();
        boolean first = true;
        for (Map.Entry<LocalDate, SnapshotRecord> entry : snapshots.entrySet()) {
            current = entry.getValue().debt().getAccount(PRINCIPAL);
            if (first || !current.equals(previous)) {
                result.put(entry.getKey(), current.orElse(null));
                previous = current;
                first = false;
            }
        }
        return result;
    }

    @Override
    protected NavigableMap<LocalDate, BigDecimal> getLoanRateRanges(ExampleCredit credit,
        Collection<OperationRecord> operations, NavigableMap<LocalDate, SnapshotRecord> snapshots,
        LocalDate to) {
        NavigableMap<LocalDate, BigDecimal> result = new TreeMap<>();
        result.put(credit.getStartDate(), credit.getCondition().getInterestRate());
        return result;
    }

    @Override
    protected Set<LocalDate> getFixAccuralDays(ExampleCredit credit, Collection<OperationRecord> operations,
        NavigableMap<LocalDate, SnapshotRecord> snapshots, LocalDate date) {
        return credit.getApplication().getPaymentSchedule().getPayments().navigableKeySet();
    }

    @Override
    protected NavigableMap<LocalDate, DayCounter> getDayCounters(ExampleCredit credit,
        Collection<OperationRecord> operations, NavigableMap<LocalDate, SnapshotRecord> snapshots,
        LocalDate date) {
        ExampleCreditCondition condition = credit.getCondition();
        LocalDate maturityDate = credit.getApplication().getPaymentSchedule().getPayments().lastKey();
        NavigableMap<LocalDate, DayCounter> counters = new TreeMap<>();
        counters.put(credit.getStartDate(), new DayCounter(
            new PaymentGrid(maturityDate, credit.getStartDate().getDayOfMonth(), condition.getPeriod()),
            dayCountMethods.get(condition.getSecuredOffer().getOriginalOffer().getCreditProduct().getDayCountMethod())));
        return counters;
    }
}
