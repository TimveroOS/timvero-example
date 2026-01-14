package com.timvero.example.admin.transaction;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.charge.ChargeOperation;
import com.timvero.example.admin.operation.charge.ChargeOperationService;
import com.timvero.example.admin.transaction.entity.BorrowerTransaction;
import com.timvero.example.admin.transaction.entity.LiquidityClientPaymentMethod;
import com.timvero.ground.util.EntityUtils;
import com.timvero.ground.util.TransactionUtils;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.transfer.method.entity.PaymentMethod;
import com.timvero.transfer.transaction.entity.PaymentTransaction;
import com.timvero.transfer.transaction.entity.PaymentTransactionRepository;
import com.timvero.transfer.transaction.entity.TransactionStatus;
import com.timvero.transfer.transaction.entity.TransactionType;
import com.timvero.transfer.transaction.service.PaymentTransactionHandler;
import com.timvero.transfer.transaction.service.PaymentTransactionService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BorrowerTransactionService implements PaymentTransactionHandler {

    @Autowired
    private PaymentTransactionService transactionService;
    @Autowired
    private PaymentTransactionRepository transactionRepository;
    @Autowired
    private ChargeOperationService chargeOperationService;

    @Transactional(propagation = Propagation.MANDATORY)
    public void proceedCustom(ExampleCredit credit, TransactionType type, PaymentMethod paymentMethod,
        MonetaryAmount amount, boolean sync, String description) {
        BorrowerTransaction transaction = new BorrowerTransaction(type, amount, paymentMethod, credit);

        transaction.setStatus(TransactionStatus.READY_FOR_EXECUTION);
        transaction.setDescription(description);

        Long transactionId = transactionRepository.save(transaction).getId();
        TransactionUtils.afterTransaction(() -> {
            if (sync) {
                transactionService.proceedSync(transactionId);
            } else {
                transactionService.proceed(transactionId);
            }
        });
    }

    @Override
    public void handle(PaymentTransaction t) {
        t = EntityUtils.initializeAndUnproxy(t);
        if (t instanceof BorrowerTransaction transaction) {
            if (transaction.getStatus() == TransactionStatus.SUCCEED) {
                ExampleCredit credit = transaction.getCredit();
                if (credit == null) {
                    credit = findApplicableCredit(transaction);
                }
                if (transaction.getOperation() != null) {
                    transaction.getOperation().setStatus(OperationStatus.APPROVED);
                }
                LocalDate date;
                if (transaction.getPaymentMethod() != null && transaction
                    .getPaymentMethod() instanceof LiquidityClientPaymentMethod LiquidityClientPaymentMethod) {
                    date = LiquidityClientPaymentMethod.getProcessedDate();
                    transaction.setService(LiquidityClientPaymentMethod.getType());
                } else {
                    date = (transaction.getCompletedAt() != null ? transaction.getCompletedAt() : Instant.now())
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                }

                if (transaction.getOperation() != null) {
                    return;
                }

                transactionRepository.saveAndFlush(transaction);
                CreditOperation operation = switch (transaction.getType()) {
                    case OUTGOING -> handleOutgoing(credit, transaction, date);
                    default -> throw new IllegalArgumentException(
                        "Unexpected transaction type: " + transaction.getType());
                };
                transaction.setOperation(operation);
            }
        }
    }

    private ExampleCredit findApplicableCredit(BorrowerTransaction transaction) {
        throw new UnsupportedOperationException();
    }

    private ChargeOperation handleOutgoing(ExampleCredit credit, BorrowerTransaction transaction, LocalDate date) {
        return chargeOperationService.createOperation(credit.getId(), date, transaction.getAmount());
    }
}
