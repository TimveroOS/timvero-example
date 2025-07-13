package com.timvero.example.admin.operation.accrual;

import static com.timvero.example.admin.credit.CreditCalculationConfiguration.INTEREST;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PRINCIPAL;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.pastdue.PastDueOperation;
import com.timvero.servicing.engine.distribution.SnapshotRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class InterestAccrualEngine extends BasisAccrualEngine {

    @Override
    public String getAccrualAccount() {
        return INTEREST;
    }

    @Override
    public Set<String> getBasisAccounts() {
        return Set.of(PRINCIPAL);
    }

    @Override
    protected Collection<LocalDate> getFixAccrualRanges(NavigableMap<LocalDate, SnapshotRecord> snapshots) {
        return getFixAccrualRanges(snapshots, Set.of(PastDueOperation.TYPE));
    }

    @Override
    public NavigableMap<LocalDate, BigDecimal> getLoanRateRanges(ExampleCredit credit, LocalDate from,
        LocalDate to) {
        NavigableMap<LocalDate, BigDecimal> result = new TreeMap<>();
        result.put(credit.getStartDate(), credit.getCondition().getInterestRate());
        return result;
    }
}
