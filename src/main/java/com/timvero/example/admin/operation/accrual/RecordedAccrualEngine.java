package com.timvero.example.admin.operation.accrual;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.servicing.credit.entity.debt.Debt;
import com.timvero.servicing.engine.accural.AccrualEngine;
import com.timvero.servicing.engine.distribution.OperationRecord;
import com.timvero.servicing.engine.distribution.SnapshotRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import javax.money.MonetaryAmount;
import org.apache.commons.math3.fraction.BigFraction;

public abstract class RecordedAccrualEngine implements AccrualEngine<ExampleCredit> {

    public abstract String getAccrualAccount();

    public abstract List<AccrualRecord> calculateAccrualRecords(ExampleCredit credit, NavigableMap<LocalDate, SnapshotRecord> snapshots,
        LocalDate date);

    protected NavigableMap<LocalDate, MonetaryAmount> getPerformedAccruals(Collection<OperationRecord> operations) {
        NavigableMap<LocalDate, MonetaryAmount> result = new TreeMap<>();
        for (OperationRecord operation : operations) {
            Optional<MonetaryAmount> amount = operation.finalDebt().map(Debt::getAccounts).map(m -> m.get(getAccrualAccount()));
            if (amount.map(MonetaryAmount::isPositive).orElse(false)) {
                MonetaryAmount a = result.get(operation.date());
                if (a == null) {
                    a = amount.get();
                } else {
                    a = a.add(amount.get());
                }
                result.put(operation.date(), a);
            }
        }
        return result;
    }

    public Optional<MonetaryAmount> calculate(ExampleCredit credit, NavigableMap<LocalDate, SnapshotRecord> snapshots,
        LocalDate date) {
        Collection<OperationRecord> operations = snapshots.values().stream().map(SnapshotRecord::operations)
            .flatMap(Collection::stream).toList();
        NavigableMap<LocalDate, MonetaryAmount> performedAccruals = getPerformedAccruals(operations);

        List<MonetaryAmount> performedNegated = performedAccruals.values().stream().map(MonetaryAmount::negate)
            .toList();
        List<MonetaryAmount> accurals = new ArrayList<>(performedNegated);

        List<AccrualRecord> details = calculateAccrualRecords(credit, snapshots, date);
        details.forEach(d -> accurals.add(d.increment()));

        return accurals.stream().reduce(MonetaryAmount::add);
    }

    public record AccrualRecord(
        LocalDate start,
        LocalDate end,
        long daysCount,
        MonetaryAmount basis,
        BigDecimal monthlyRate,
        BigFraction fraction,
        MonetaryAmount increment
    ){}

    @Override
    public Debt calculateAccurals(ExampleCredit credit, NavigableMap<LocalDate, SnapshotRecord> snapshots,
        LocalDate date) {
        Optional<MonetaryAmount> accrual = calculate(credit, snapshots, date);
        return accrual.map(amount -> new Debt(Map.of(getAccrualAccount(), amount))).orElse(Debt.ZERO);
    }
}
