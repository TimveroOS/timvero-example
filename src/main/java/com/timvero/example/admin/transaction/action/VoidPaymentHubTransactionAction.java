package com.timvero.example.admin.transaction.action;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.credit.entity.ExampleCreditRepository;
import com.timvero.ground.action.EntityAction;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.servicing.engine.CreditCalculationService;
import com.timvero.transfer.paymenthub.entity.PaymentHubTransaction;
import com.timvero.transfer.paymenthub.entity.PaymentHubTransactionRepository;
import com.timvero.transfer.transaction.entity.TransactionStatus;
import com.timvero.web.common.action.EntityActionController;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/void-paid")
public class VoidPaymentHubTransactionAction extends EntityActionController<Long, PaymentHubTransaction, Object> {

    @Autowired
    private CreditCalculationService creditCalculationService;
    @Autowired
    private ExampleCreditRepository creditRepository;
    @Autowired
    private PaymentHubTransactionRepository transactionRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    protected EntityAction<? super PaymentHubTransaction, Object> action() {
        return when(t -> t.getStatus().isSuccessful() && t.getStatus().isComplete())
            .then((t, f, u) -> {
                Assert.state(t.getStatus().isComplete(),
                    "Only completed transactions can be voided");

                t.setStatus(TransactionStatus.VOIDED);
                List<CreditPayment> payments = t.getPayments().stream()
                    .filter(p -> p.getStatus() == OperationStatus.APPROVED).toList();

                payments.forEach(p -> p.setStatus(OperationStatus.CANCELED));
                transactionRepository.saveAndFlush(t);
                entityManager.clear();

                for (CreditPayment payment : payments) {
                    ExampleCredit credit = creditRepository.findByOperationsIn(payment);
                    creditCalculationService.calculate(credit.getId(), credit.getStartDate(), credit.getCalculationDate());
                }
            });
    }

    @Override
    protected String getActionTemplate(Long id, Model model, String actionPath) throws Exception {
        model.addAttribute("title", "transaction.dialog.action.void.title");
        model.addAttribute("message", "transaction.dialog.action.void.message");
        return "/common/action/yes-no";
    }
}
