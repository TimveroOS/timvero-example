package com.timvero.example.admin.product.engine;

import static com.timvero.servicing.engine.CreditCalculatorUtils.append;
import static com.timvero.servicing.engine.CreditCalculatorUtils.calcInterest;
import static com.timvero.servicing.engine.CreditCalculatorUtils.periodicInterest;

import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.ground.util.Lang;
import com.timvero.loan.engine.scheduled.ScheduledEngine;
import com.timvero.loan.engine.util.PaymentCalculator;
import com.timvero.scheduled.entity.PaymentSegment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.money.MonetaryAmount;
import org.springframework.stereotype.Service;

@Service(SimpleScheduledEngine.NAME)
public class SimpleScheduledEngine implements ScheduledEngine<ExampleCreditCondition> {

    public static final String NAME = "SimpleScheduledEngine";

    @Override
    public List<PaymentSegment> payments(ExampleCreditCondition condition, LocalDate interestStart,
        LocalDate paymentStart, MonetaryAmount principal, MonetaryAmount interest) {
        double overestimate = 0;//14/28D;

        BigDecimal annualInterest;
        annualInterest = condition.getAnnualInterest();

        MonetaryAmount regularPayment = PaymentCalculator.calcAnnuityPayment(principal, interest,
            periodicInterest(condition.getPeriod(), annualInterest), condition.getTerm(),
            overestimate);

        return payments(regularPayment, annualInterest,
            condition.getPeriod(), paymentStart.getDayOfMonth(), principal, interest, interestStart, paymentStart,
            condition.getTerm());
    }

    @Override
    public Class<ExampleCreditCondition> getConditionClass() {
        return ExampleCreditCondition.class;
    }

    private List<PaymentSegment> payments(MonetaryAmount regularPayment, BigDecimal annualInterest,
        Period period, int day, MonetaryAmount principal, MonetaryAmount interest,
        LocalDate startInterest, LocalDate payStarting, int length) {

        List<PaymentSegment> payments = new ArrayList<>();

        int shift = 0;
        LocalDate firstPaymentAt;

        while (ChronoUnit.DAYS.between(payStarting,
            firstPaymentAt = append(payStarting, period, shift, day)) < 0) {
            shift++;
        }

        int order = 0;
        int orderDiscount = 0;

        LocalDate from = startInterest, to;
        while (order < length) {

            MonetaryAmount principalPayment;
            MonetaryAmount interestPayment;

            to = append(firstPaymentAt, period, order, day);

            BigDecimal currentInterest = annualInterest;

            if (from.isBefore(to)) {
                interest = interest.add(calcInterest(currentInterest, principal, from, from, to, period, day));
            }

            order++;
            if (order < length) {
                interestPayment = Lang.min(regularPayment, interest);
                principalPayment = Lang.min(principal, regularPayment.subtract(interestPayment));
            } else {
                interestPayment = interest;
                principalPayment = principal;
            }

            principal = principal.subtract(principalPayment);
            interest = interest.subtract(interestPayment);

            PaymentSegment payment = new PaymentSegment(to, principalPayment, interestPayment, principal, interest);
            payments.add(payment);
            from = Lang.max(to, from);
            if (principal.isNegativeOrZero()) {
                break;
            }
        }

        return payments;
    }
}
