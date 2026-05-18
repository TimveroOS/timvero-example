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
import org.springframework.context.annotation.Lazy;
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
    @Autowired
    @Lazy
    private BorrowerTransactionService self;

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
        if (t instanceof BorrowerTransaction transaction
            && transaction.getStatus() == TransactionStatus.SUCCEED) {
            Long transactionId = transaction.getId();
            TransactionUtils.afterTransaction(() -> self.applyOperation(transactionId));
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void applyOperation(Long transactionId) {
        BorrowerTransaction transaction = (BorrowerTransaction) transactionRepository.getSync(transactionId);
        if (transaction.getStatus() != TransactionStatus.SUCCEED) {
            return;
        }
        if (transaction.getOperation() != null) {
            transaction.getOperation().setStatus(OperationStatus.APPROVED);
            return;
        }
        ExampleCredit credit = transaction.getCredit();
        if (credit == null) {
            credit = findApplicableCredit(transaction);
        }
        LocalDate date;
        if (transaction.getPaymentMethod() instanceof LiquidityClientPaymentMethod lcpm) {
            date = lcpm.getProcessedDate();
        } else {
            date = (transaction.getCompletedAt() != null ? transaction.getCompletedAt() : Instant.now())
                .atZone(ZoneId.systemDefault()).toLocalDate();
        }
        CreditOperation operation = switch (transaction.getType()) {
            case OUTGOING -> handleOutgoing(credit, transaction, date);
            default -> throw new IllegalArgumentException(
                "Unexpected transaction type: " + transaction.getType());
        };
        if (transaction.getPaymentMethod() instanceof LiquidityClientPaymentMethod lcpm) {
            transaction.setService(lcpm.getType());
        }
        transaction.setOperation(operation);
    }

    private ExampleCredit findApplicableCredit(BorrowerTransaction transaction) {
        throw new UnsupportedOperationException();
    }

    private ChargeOperation handleOutgoing(ExampleCredit credit, BorrowerTransaction transaction, LocalDate date) {
        return chargeOperationService.createOperation(credit.getId(), date, transaction.getAmount());
    }
}
