package com.timvero.example.admin.credit;

import com.timvero.servicing.credit.entity.CreditStatus;
import com.timvero.servicing.engine.general.BasicLoanEngine;
import com.timvero.servicing.engine.general.LoanEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreditCalculationConfiguration {

    public static final CreditStatus VOID = new CreditStatus("VOID", 1000, false);

    @Bean
    public LoanEngine loanEngine() {
        return new BasicLoanEngine(VOID);
    }

}
