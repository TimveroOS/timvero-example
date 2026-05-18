package com.timvero.example.admin.operation.accrual;

import static com.timvero.example.admin.credit.CreditCalculationConfiguration.LATE_FEE;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PAST_DUE_INTEREST;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PAST_DUE_PRINCIPAL;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.charge.ChargeOperation;
import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.servicing.credit.entity.debt.Debt;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.servicing.engine.CreditCalculatorUtils;
import com.timvero.servicing.engine.accural.AccrualEngine;
import com.timvero.servicing.engine.distribution.SnapshotRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import javax.money.MonetaryAmount;

public class LateFeeAccrualEngine implements AccrualEngine<ExampleCredit> {

    @Override
    public Debt calculateAccurals(ExampleCredit credit, NavigableMap<LocalDate, SnapshotRecord> snapshots,
        LocalDate date) {
        ExampleCreditCondition condition = credit.getCondition();

        Debt accurals = Debt.ZERO;

        LocalDate interestStart = lastInterestChargeDate(credit, date);
        if (interestStart == null) {
            return accurals;
        }

        LocalDate startFee = interestStart;
        Optional<MonetaryAmount> pastDue;
        Map.Entry<LocalDate, SnapshotRecord> entry = snapshots.floorEntry(interestStart);
        if (entry != null) {
            pastDue = pastDueOf(entry.getValue());
        } else {
            pastDue = Optional.empty();
        }

        for (SnapshotRecord next : snapshots.tailMap(startFee, false).values()) {
            Optional<MonetaryAmount> nextPastDue = pastDueOf(next);

            if (!pastDue.equals(nextPastDue)) {
                if (pastDue.isPresent() && pastDue.get().isPositive()) {
                    MonetaryAmount lateFee = calcRangeLateFee(condition.getLateFeeRate(), pastDue.get(), startFee,
                        next.date(), condition.getPeriod(), credit.getStartDate().getDayOfMonth());
                    accurals = accurals.add(new Debt(Map.of(LATE_FEE, lateFee)));
                }
                pastDue = nextPastDue;
                startFee = next.date();
            }
        }

        if (pastDue.isPresent() && pastDue.get().isPositive()) {
            MonetaryAmount lateFee = calcRangeLateFee(condition.getLateFeeRate(), pastDue.get(), startFee, date,
                condition.getPeriod(), credit.getStartDate().getDayOfMonth());
            accurals = accurals.add(new Debt(Map.of(LATE_FEE, lateFee)));
        }

        return accurals;
    }

    private Optional<MonetaryAmount> pastDueOf(SnapshotRecord snapshot) {
        Debt debt = snapshot.debt();
        Optional<MonetaryAmount> principal = debt.getAccount(PAST_DUE_PRINCIPAL);
        Optional<MonetaryAmount> interest = debt.getAccount(PAST_DUE_INTEREST);
        if (principal.isPresent() && interest.isPresent()) {
            return Optional.of(principal.get().add(interest.get()));
        }
        return principal.isPresent() ? principal : interest;
    }

    private LocalDate lastInterestChargeDate(ExampleCredit credit, LocalDate date) {
        Optional<LocalDate> charged = credit.getOperations().stream()
            .filter(o -> o.getStatus() == OperationStatus.APPROVED)
            .filter(o -> o.getDate().isBefore(date))
            .filter(o -> o.getType().equals(ChargeOperation.TYPE))
            .map(CreditOperation::getDate).min(Comparator.naturalOrder());
        if (charged.isEmpty()) {
            return null;
        }
        Optional<LocalDate> accural = credit.getOperations().stream()
            .filter(o -> o.getStatus() == OperationStatus.APPROVED)
            .filter(o -> o.getDate().isBefore(date))
            .filter(o -> o.getType().equals(AccrualOperation.TYPE))
            .map(CreditOperation::getDate).max(Comparator.naturalOrder());
        return accural.orElseGet(charged::get);
    }

    private MonetaryAmount calcRangeLateFee(BigDecimal rate, MonetaryAmount principal, LocalDate rangeStart,
        LocalDate rangeEnd, Period period, int paymentDay) {
        return CreditCalculatorUtils.calcInterest(rate, principal, rangeStart, rangeStart, rangeEnd, period, paymentDay);
    }
}