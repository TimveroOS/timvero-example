package com.timvero.example.admin.transaction;

import static com.timvero.transfer.transaction.entity.TransactionType.INCOMING;
import static com.timvero.transfer.transaction.entity.TransactionType.OUTGOING;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.charge.ChargeOperation;
import com.timvero.example.admin.operation.charge.ChargeOperationService;
import com.timvero.example.admin.operation.payment.ExampleCreditPayment;
import com.timvero.example.admin.transaction.entity.BorrowerTransaction;
import com.timvero.example.admin.transaction.entity.BorrowerTransactionRepository;
import com.timvero.example.admin.transaction.entity.LiquidityClientPaymentMethod;
import com.timvero.ground.util.EntityUtils;
import com.timvero.ground.util.TransactionUtils;
import com.timvero.servicing.credit.entity.CoreCreditRepository;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.servicing.engine.CreditPaymentService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class BorrowerTransactionService implements PaymentTransactionHandler {

    public static final String BANK_TRANSACTION_SERVICE = "BANK";

    @Autowired
    private PaymentTransactionService transactionService;
    @Autowired
    private CoreCreditRepository creditRepository;
    @Autowired
    private PaymentTransactionRepository transactionRepository;
    @Autowired
    private BorrowerTransactionRepository borrowerTransactionRepository;
    @Autowired
    private CreditPaymentService paymentService;
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
                    case INCOMING -> handleIncoming(credit, transaction, date);
                    case OUTGOING -> handleOutgoing(credit, transaction, date);
                    default -> throw new IllegalArgumentException(
                        "Unexpected transaction type: " + transaction.getType());
                };
                transaction.setOperation(operation);
            }
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void registerRefund(CreditPayment payment) {
        Collection<BorrowerTransaction> transactions = borrowerTransactionRepository.findAllByOperation(payment);
        List<BorrowerTransaction> updatedTransactions = new ArrayList<>();

        for (BorrowerTransaction transaction : transactions) {
            transaction.setStatus(TransactionStatus.VOIDED);
            updatedTransactions.add(transaction);
        }

        transactionRepository.saveAll(updatedTransactions);
    }

    private ExampleCredit findApplicableCredit(BorrowerTransaction transaction) {
        throw new UnsupportedOperationException();
    }

    private CreditPayment handleIncoming(ExampleCredit credit, BorrowerTransaction transaction, LocalDate date) {
        if (transaction.getPaymentMethod().getType().equals(LiquidityClientPaymentMethod.TYPE)) {
            LiquidityClientPaymentMethod paymentMethod = (LiquidityClientPaymentMethod) transaction.getPaymentMethod();
            CreditPayment payment = new ExampleCreditPayment(date, paymentMethod.getAmount());
            return paymentService.registerPayment(credit, payment);
        }
        throw new IllegalArgumentException("Unknown payment method type: " + transaction.getPaymentMethod().getType());
    }

    private ChargeOperation handleOutgoing(ExampleCredit credit, BorrowerTransaction transaction, LocalDate date) {
        return chargeOperationService.createOperation(credit.getId(), date, transaction.getAmount());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean registerChargeback(BorrowerTransaction transaction) {
        return failTransaction(transaction, TransactionStatus.CHARGEBACK);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean failTransaction(BorrowerTransaction transaction) {
        return failTransaction(transaction, TransactionStatus.FAILED);
    }

    private boolean failTransaction(BorrowerTransaction transaction, TransactionStatus failStatus) {
        if (transaction.getType() == INCOMING
            && transaction.getStatus().in(TransactionStatus.IN_PROGRESS, TransactionStatus.SUCCEED)) {
            transaction.setStatus(failStatus);
            CreditPayment payment = (CreditPayment) transaction.getOperation();
            if (payment != null) {
                paymentService.refundPayment(payment, payment.getAmount());
            }
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public BorrowerTransaction registerManualRefund(CreditPayment payment, MonetaryAmount amount,
        String description) {
        if (amount == null) {
            amount = payment.getAmount();
        }
        Assert.isTrue(payment.getStatus().in(OperationStatus.APPROVED),
            "Payment must be active; payment " + payment.getId()); // is it correct???
        Assert.isTrue(amount.signum() > 0,
            "Payment amount must be positive: " + amount + "; payment " + payment.getId());
        Assert.isTrue(payment.getAmount().compareTo(amount) >= 0,
            "Not enough money: " + amount + "; payment " + payment.getId());

        BorrowerTransaction transaction = new BorrowerTransaction(OUTGOING, amount, null,
            (ExampleCredit) creditRepository.findByOperationsIn(payment));
        transaction.setService(BANK_TRANSACTION_SERVICE);
        transaction.setDescription(description);
        transaction.setStatus(TransactionStatus.SUCCEED);
        transaction.setOperation(payment);

        paymentService.refundPayment(payment, amount);

        return EntityUtils.initializeAndUnproxy(transactionRepository.save(transaction));
    }

}
