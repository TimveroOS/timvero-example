package com.timvero.example.admin.operation.accrual;

import static com.timvero.example.admin.credit.CreditCalculationConfiguration.LATE_FEE;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PAST_DUE_INTEREST;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PAST_DUE_PRINCIPAL;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import java.math.BigDecimal;
import java.util.Set;

public class LateFeeAccrualEngine extends AbstractAccrualEngine {

    @Override
    public String getAccrualAccount() {
        return LATE_FEE;
    }

    public Set<String> getBasisAccounts() {
        return Set.of(PAST_DUE_PRINCIPAL, PAST_DUE_INTEREST);
    }

    @Override
    protected BigDecimal getFeeRate(ExampleCredit credit) {
        return credit.getCondition().getLateFeeRate();
    }

}
