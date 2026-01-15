package com.timvero.example.admin.credit;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.operation.accrual.AccrualOperationService;
import com.timvero.example.admin.operation.accrual.InterestAccrualEngine;
import com.timvero.example.admin.operation.accrual.LateFeeAccrualEngine;
import com.timvero.example.admin.operation.charge.ChargeOperationService;
import com.timvero.example.admin.operation.pastdue.PastDueOperationService;
import com.timvero.servicing.credit.CreditViewOptions;
import com.timvero.servicing.credit.entity.CreditStatus;
import com.timvero.servicing.credit.entity.operation.CreditPaymentType;
import com.timvero.servicing.engine.general.BasicLoanEngine;
import com.timvero.servicing.engine.general.LoanEngine;
import com.timvero.servicing.engine.operation.payment.CreditPaymentOperationHandler;
import com.timvero.transfer.paymenthub.entity.PaymentTransactionPurpose;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreditCalculationConfiguration {

    // tag::payment-hub-config[]
    public static final CreditPaymentType GENERAL = new CreditPaymentType("GENERAL");

    public static final PaymentTransactionPurpose<Client> GENERAL_PURPOSE = new PaymentTransactionPurpose<>("GENERAL", Client.class);
    // end::payment-hub-config[]

    public static final CreditStatus PENDING = new CreditStatus("PENDING", 1000, false);
    public static final CreditStatus ACTIVE = new CreditStatus("ACTIVE", 1100, false);
    public static final CreditStatus CLOSED = new CreditStatus("CLOSED", 2000, true);
    public static final CreditStatus VOID = new CreditStatus("VOID", 2100, true);

    // tag::account-types[]
    public static final String PRINCIPAL = "PRINCIPAL";
    public static final String INTEREST = "INTEREST";
    public static final String PAST_DUE_PRINCIPAL = "PD_PRINCIPAL";
    public static final String PAST_DUE_INTEREST = "PD_INTEREST";
    public static final String LATE_FEE = "LATE_FEE";
    public static final String OVERPAYMENT = "OVERPAYMENT";
    // end::account-types[]

    @Bean
    CreditViewOptions creditViewOptions() {
        return new CreditViewOptions(PRINCIPAL, INTEREST, PAST_DUE_PRINCIPAL, PAST_DUE_INTEREST, LATE_FEE);
    }

    @Bean
    LoanEngine loanEngine() {
        return new BasicLoanEngine(PENDING);
    }

    @Bean
    ChargeOperationService chargeOperationService() {
        return new ChargeOperationService();
    }

    @Bean
    AccrualOperationService accrualOperationService() {
        return new AccrualOperationService();
    }

    @Bean
    PastDueOperationService pastDueOperationService() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(INTEREST, PAST_DUE_INTEREST);
        map.put(PRINCIPAL, PAST_DUE_PRINCIPAL);
        return new PastDueOperationService(map);
    }

    // tag::payment-distribution[]
    @Bean
    CreditPaymentOperationHandler creditPaymentOperationHandler() {
        return new CreditPaymentOperationHandler(OVERPAYMENT, List.of(PAST_DUE_PRINCIPAL, PAST_DUE_INTEREST,
            LATE_FEE, INTEREST, PRINCIPAL)) {};
    }
    // end::payment-distribution[]

    @Bean
    InterestAccrualEngine interestAccrualEngine() {
        return new InterestAccrualEngine();
    }

    @Bean
    LateFeeAccrualEngine lateFeeAccrualEngine() {
        return new LateFeeAccrualEngine();
    }

    @Bean
    CreditPaymentService creditPaymentService() {
        return new CreditPaymentService();
    }
}
