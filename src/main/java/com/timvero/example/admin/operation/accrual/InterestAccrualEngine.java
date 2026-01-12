package com.timvero.example.admin.operation.accrual;

import static com.timvero.example.admin.credit.CreditCalculationConfiguration.INTEREST;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PRINCIPAL;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import java.math.BigDecimal;
import java.util.Set;

public class InterestAccrualEngine extends AbstractAccrualEngine {

    @Override
    public String getAccrualAccount() {
        return INTEREST;
    }

    public Set<String> getBasisAccounts() {
        return Set.of(PRINCIPAL);
    }

    @Override
    protected BigDecimal getFeeRate(ExampleCredit credit) {
        return credit.getCondition().getInterestRate();
    }
}
