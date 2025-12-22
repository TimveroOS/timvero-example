package com.timvero.example.admin.credit.action;

import com.timvero.example.admin.credit.CreditPaymentService;
import com.timvero.example.admin.transaction.BorrowerTransactionService;
import com.timvero.ground.action.EntityAction;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.web.common.action.SimpleActionController;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/refund")
@Order(5_000)
public class RegisterRefundAction extends SimpleActionController<UUID, CreditPayment> {

    @Autowired
    private CreditPaymentService paymentService;

    @Autowired
    private BorrowerTransactionService borrowerTransactionService;

    @Override
    public String getHighlighted() {
        return BTN_SUCCESS;
    }

    @Override
    protected EntityAction<? super CreditPayment, Object> action() {
        return when(payment -> payment.getStatus() == OperationStatus.APPROVED)
            .then((payment, form, user) -> {
                paymentService.refundPayment(payment, null);
                borrowerTransactionService.registerRefund(payment);
            });
    }
}
