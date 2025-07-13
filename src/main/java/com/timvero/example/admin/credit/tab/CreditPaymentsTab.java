package com.timvero.example.admin.credit.tab;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.servicing.credit.CreditViewOptions;
import com.timvero.servicing.credit.entity.Credit;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.servicing.credit.tab.AbstractCreditPaymentsTab;
import java.util.stream.Stream;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/payments")
@Controller
@Order(7300)
@ConditionalOnBean(CreditViewOptions.class)
public class CreditPaymentsTab extends AbstractCreditPaymentsTab<CreditPayment, ExampleCredit> {

    @Override
    protected Stream<CreditPayment> getPayments(Credit credit) {
        return credit.getOperations(CreditPayment.class, OperationStatus.APPROVED, OperationStatus.CANCELED);
    }

    @Override
    public boolean isVisible(ExampleCredit entity) {
        return true;
    }
}
