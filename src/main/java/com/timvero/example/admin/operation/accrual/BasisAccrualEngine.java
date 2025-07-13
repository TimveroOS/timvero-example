package com.timvero.example.admin.operation.accrual;

import static com.timvero.servicing.credit.entity.operation.OperationStatus.APPROVED;
import static com.timvero.servicing.engine.CreditCalculatorUtils.calcRangeAccural;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.scheduled.day_count.DayCountMethod;
import com.timvero.scheduled.day_count.PaymentGrid;
import com.timvero.servicing.credit.entity.debt.Debt;
import com.timvero.servicing.engine.accural.AbstractAccrualEngine.DayCounter;
import com.timvero.servicing.engine.distribution.OperationRecord;
import com.timvero.servicing.engine.distribution.SnapshotRecord;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.money.MonetaryAmount;
import org.apache.commons.math3.fraction.BigFraction;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BasisAccrualEngine extends RecordedAccrualEngine {

    private static final int SCALE = 10;

    @Autowired
    protected Map<String, DayCountMethod> dayCountMethods;

    public abstract Set<String> getBasisAccounts();

    @Override
    public List<AccrualRecord> calculateAccrualRecords(ExampleCredit credit,
        NavigableMap<LocalDate, SnapshotRecord> snapshots, LocalDate date) {

        ExampleCreditCondition condition = credit.getCondition();
        NavigableMap<LocalDate, MonetaryAmount> basisRanges = getBasisRanges(condition, snapshots);
        if (basisRanges.isEmpty()) {
            return Collections.emptyList();
        }
        LocalDate maturityDate = credit.getMaturityDate();
        NavigableMap<LocalDate, BigDecimal> loanRates =
            getLoanRateRanges(credit, credit.getStartDate(), maturityDate);
        final PaymentGrid paymentGrid =
            new PaymentGrid(maturityDate, credit.getStartDate().getDayOfMonth(), PaymentGrid.MONTHLY);
        DayCountMethod dayCountMethod = dayCountMethods.get(credit.getCondition().getDayCountMethod());
        final DayCounter dayCounter = new DayCounter(paymentGrid, dayCountMethod);

        NavigableSet<LocalDate> ranges = new TreeSet<>();
        ranges.addAll(basisRanges.keySet());
        ranges.addAll(loanRates.keySet());
        ranges.addAll(getFixAccrualRanges(snapshots));
        ranges.add(date);

        LocalDate start = basisRanges.firstKey();

        ranges = ranges.subSet(start, false, date, true);

        List<AccrualRecord> accruals = new ArrayList<>();

        for (LocalDate end : ranges) {
            Optional<MonetaryAmount> basis = Optional.ofNullable(basisRanges.lowerEntry(end)).map(Map.Entry::getValue);
            Optional<BigDecimal> rate = Optional.ofNullable(loanRates.lowerEntry(end)).map(Map.Entry::getValue);
            if (basis.isPresent() && rate.isPresent()) {
                BigFraction fraction = dayCounter.count(start, end);
                MonetaryAmount increment = calcRangeAccural(basis.get(), rate.get(), fraction);
                long daysCount = dayCountMethod.countDays(start, end, paymentGrid);
                BigDecimal monthlyRate = rate.get().divide(BigDecimal.valueOf(12L), SCALE, RoundingMode.HALF_UP);
                accruals.add(new AccrualRecord(start, end, daysCount, basis.get(), monthlyRate, fraction, increment));
            }
            start = end;
        }
        return accruals;
    }

    protected abstract Collection<LocalDate> getFixAccrualRanges(NavigableMap<LocalDate, SnapshotRecord> snapshots);

    protected Collection<LocalDate> getFixAccrualRanges(NavigableMap<LocalDate, SnapshotRecord> snapshots,
        Set<Integer> operationTypes) {
        return snapshots.values().stream().map(SnapshotRecord::operations).flatMap(Collection::stream)
            .filter(o -> o.operation().getStatus() == APPROVED)
            .filter(o -> operationTypes.contains(o.operation().getType()))
            .sorted(Comparator.comparing(OperationRecord::date)).map(OperationRecord::date).toList();
    }



    public NavigableMap<LocalDate, MonetaryAmount> getBasisRanges(ExampleCreditCondition condition,
        NavigableMap<LocalDate, SnapshotRecord> snapshots) {
        TreeMap<LocalDate, MonetaryAmount> result = new TreeMap<>();
        Optional<MonetaryAmount> current, previous = Optional.empty();
        for (Map.Entry<LocalDate, SnapshotRecord> entry : snapshots.entrySet()) {
            Debt debt = entry.getValue().debt();
            current = getBasisAccounts().stream().map(debt::getAccount).flatMap(Optional::stream)
                .reduce(MonetaryAmount::add);
            if (!current.equals(previous)) {
                result.put(entry.getKey(), current.orElse(null));
                previous = current;
            }
        }
        return result;
    }

    public abstract NavigableMap<LocalDate, BigDecimal> getLoanRateRanges(ExampleCredit credit, LocalDate from,
        LocalDate to);
}
