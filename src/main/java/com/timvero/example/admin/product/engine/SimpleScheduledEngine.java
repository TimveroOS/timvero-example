package com.timvero.example.admin.product.engine;

import static com.timvero.servicing.engine.CreditCalculatorUtils.calcRangeAccural;

import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.ground.util.Lang;
import com.timvero.loan.engine.scheduled.ScheduledEngine;
import com.timvero.scheduled.day_count.DayCountMethod;
import com.timvero.scheduled.day_count.DayCounter;
import com.timvero.scheduled.day_count.PaymentGrid;
import com.timvero.scheduled.entity.PaymentSegment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.money.MonetaryAmount;
import org.apache.commons.math3.fraction.BigFraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(SimpleScheduledEngine.NAME)
public class SimpleScheduledEngine implements ScheduledEngine<ExampleCreditCondition> {

    public static final String NAME = "SimpleScheduledEngine";

    @Autowired
    protected Map<String, DayCountMethod> dayCountMethods;

    @Override
    public Class<ExampleCreditCondition> getConditionClass() {
        return ExampleCreditCondition.class;
    }

    @Override
    public List<PaymentSegment> payments(ExampleCreditCondition condition, LocalDate interestStart,
        LocalDate paymentStart, MonetaryAmount principal, MonetaryAmount interest) {
        DayCountMethod dayCountMethod = dayCountMethods.get(condition.getDayCountMethod());
        return payments(condition.getRegularPayment(), condition.getInterestRate(),
            condition.getPeriod(), principal, interest, interestStart, paymentStart,
            condition.getTerm(), dayCountMethod);
    }

    private List<PaymentSegment> payments(MonetaryAmount regularPayment, BigDecimal annualInterest,
        Period period, MonetaryAmount principal, MonetaryAmount interest,
        LocalDate startInterest, LocalDate payStarting, int length, DayCountMethod dayCountMethod) {

        LocalDate maturityDate = startInterest.plus(period.multipliedBy(length));
        final PaymentGrid paymentGrid = new PaymentGrid(maturityDate, startInterest.getDayOfMonth(), period);
        final DayCounter dayCounter = new DayCounter(paymentGrid, dayCountMethod);

        List<PaymentSegment> payments = new ArrayList<>();

        LocalDate from = startInterest, to;
        for (int order = 1; order <= length; order++) {

            MonetaryAmount principalPayment;
            MonetaryAmount interestPayment;

            to = startInterest.plus(period.multipliedBy(order));

            if (from.isBefore(to)) {
                BigFraction fraction = dayCounter.count(from, to);
                MonetaryAmount increment = calcRangeAccural(principal, annualInterest, fraction);
                interest = interest.add(increment);
            }

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
