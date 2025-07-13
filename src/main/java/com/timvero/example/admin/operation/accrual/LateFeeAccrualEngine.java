package com.timvero.example.admin.operation.accrual;

import static com.timvero.example.admin.credit.CreditCalculationConfiguration.LATE_FEE;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PAST_DUE_INTEREST;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PAST_DUE_PRINCIPAL;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.pastdue.PastDueOperation;
import com.timvero.servicing.engine.distribution.SnapshotRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class LateFeeAccrualEngine extends BasisAccrualEngine {

    @Override
    public String getAccrualAccount() {
        return LATE_FEE;
    }

    @Override
    public Set<String> getBasisAccounts() {
        return Set.of(PAST_DUE_PRINCIPAL, PAST_DUE_INTEREST);
    }

    @Override
    protected Collection<LocalDate> getFixAccrualRanges(NavigableMap<LocalDate, SnapshotRecord> snapshots) {
        return getFixAccrualRanges(snapshots, Set.of(PastDueOperation.TYPE));
    }

    @Override
    public NavigableMap<LocalDate, BigDecimal> getLoanRateRanges(ExampleCredit credit, LocalDate from,
        LocalDate to) {
        NavigableMap<LocalDate, BigDecimal> result = new TreeMap<>();
        result.put(credit.getStartDate(), credit.getCondition().getLateFeeRate());
        return result;
    }
}
