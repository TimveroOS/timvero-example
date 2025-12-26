package com.timvero.example.admin.credit;

import com.timvero.example.admin.credit.entity.ExampleCreditRepository;
import com.timvero.ground.util.Lang;
import com.timvero.servicing.credit.entity.Credit;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.servicing.engine.CreditCalculationService;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class CreditPaymentService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ExampleCreditRepository creditRepository;

    @Autowired
    private CreditCalculationService calculationService;

    //@todo it breaks action  @Transactional(propagation = Propagation.MANDATORY)
    public Credit getCreditForPayment(CreditPayment payment) {
        return creditRepository.findByOperationsIn(payment);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean movePaymentDate(CreditPayment payment, LocalDate newDate) {
        Credit credit = creditRepository.findByOperationsIn(payment);
        LocalDate calcDate = Lang.min(payment.getDate(), newDate);
        payment.setDate(newDate);
        calculationService.calculate(credit.getId(), calcDate, credit.getCalculationDate());
        return true;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public MonetaryAmount refundPayment(CreditPayment payment, MonetaryAmount amount) {
        Assert.isTrue(payment.isEnabled(), "Payment must be active; payment " + payment.getId());
        Assert.isTrue(amount == null || amount.signum() > 0,
            "Payment amount must be positive: " + amount + "; payment " + payment.getId());

        Credit credit = creditRepository.findByOperationsIn(payment);

        MonetaryAmount paymentAmount = payment.getAmount();
        MonetaryAmount refundAmount = amount != null ? amount : paymentAmount;

        if (refundAmount.compareTo(paymentAmount) <= 0) {
            payment.setAmount(paymentAmount.subtract(refundAmount));
            if (payment.getAmount().isZero()) {
                payment.setStatus(OperationStatus.CANCELED);
            }
            calculationService.calculate(credit.getId(), payment.getDate(), credit.getCalculationDate());
            return refundAmount;
        } else {
            throw new IllegalArgumentException("Not enough money: " + amount + "; payment " + payment.getId());
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public CreditPayment registerPayment(Credit credit, CreditPayment payment) {
        credit.getOperations().add(payment);
        entityManager.flush();

        calculationService.calculate(credit.getId(), payment.getDate(), credit.getCalculationDate());
        return payment;
    }
}
