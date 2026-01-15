package com.timvero.example.admin.client;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.credit.CreditCalculationConfiguration;
import com.timvero.example.admin.credit.DebtService;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRole;
import com.timvero.servicing.credit.entity.Credit;
import com.timvero.servicing.credit.entity.debt.Debt;
import com.timvero.transfer.paymenthub.AllocationPlanProvider;
import com.timvero.transfer.paymenthub.AllocationTargetProvider;
import com.timvero.transfer.paymenthub.entity.PaymentTransactionPurpose;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// tag::class-definition[]
// tag::target-provider[]
// tag::plan-provider[]
@Service
public class ClientAllocationPlannerImpl implements AllocationPlanProvider<Client>, AllocationTargetProvider<Client> {

    // end::class-definition[]
    // end::target-provider[]
    // end::plan-provider[]

    @Autowired
    private DebtService debtService;

    public List<? extends Credit> provideAllocationTargets(LocalDate forDay, PaymentTransactionPurpose<Client> purpose,
        Client client, CurrencyUnit currency) {
        return client.getParticipants().stream()
            .filter(p -> p.getRoles().contains(ParticipantRole.BORROWER))
            .map(Participant::getApplication)
            .map(Application::getCredit)
            .filter(Objects::nonNull)
            .filter(this::isActive)
            .filter(c -> c.getCondition().getPrincipal().getCurrency().equals(currency))
            .toList();
    }

    @Override
    public List<AllocationPlanItem> provideAllocationPlan(LocalDate forDay, List<? extends Credit> credits,
        PaymentTransactionPurpose<Client> purpose, Client client, CurrencyUnit currency) {

        List<AllocationPlanItem> plan = new ArrayList<>();

        @SuppressWarnings("unchecked")
        List<ExampleCredit> fifoSortedCredits = (List<ExampleCredit>) credits.stream()
            .filter(c -> !c.getActualSnapshot().getStatus().isEnding())
            .sorted(Comparator.comparing(Credit::getStartDate))
            .toList();

        for (ExampleCredit credit : fifoSortedCredits) {
            addOverdueItems(plan, credit, forDay);
        }

        for (ExampleCredit credit : fifoSortedCredits) {
            addInterestItem(plan, credit, forDay);
            addPrincipalItem(plan, credit, forDay);
        }

        return plan;
    }

    private void addOverdueItems(List<AllocationPlanItem> plan, ExampleCredit credit, LocalDate forDay) {
        Optional<Debt> debtOpt = debtService.getTotalDebtByDate(credit, forDay);
        if (debtOpt.isEmpty()) {
            return;
        }
        Debt debt = debtOpt.get();

        List<String> overdueAccounts = List.of(
            CreditCalculationConfiguration.LATE_FEE,
            CreditCalculationConfiguration.PAST_DUE_INTEREST,
            CreditCalculationConfiguration.PAST_DUE_PRINCIPAL
        );

        for (String accountKey : overdueAccounts) {
            debt.getAccount(accountKey)
                .filter(MonetaryAmount::isPositive)
                .ifPresent(amount -> plan.add(new AllocationPlanItem(
                    credit.getId(),
                    credit.getShortId(),
                    "Overdue: " + accountKey,
                    CreditCalculationConfiguration.GENERAL,
                    amount
                )));
        }
    }

    private void addInterestItem(List<AllocationPlanItem> plan, ExampleCredit credit, LocalDate forDay) {
        debtService.getTotalDebtByDate(credit, forDay)
            .flatMap(debt -> debt.getAccount(CreditCalculationConfiguration.INTEREST))
            .filter(MonetaryAmount::isPositive)
            .ifPresent(amount -> plan.add(new AllocationPlanItem(
                credit.getId(),
                credit.getShortId(),
                "Accrued interest as of " + forDay,
                CreditCalculationConfiguration.GENERAL,
                amount
            )));
    }

    private void addPrincipalItem(List<AllocationPlanItem> plan, ExampleCredit credit, LocalDate forDay) {
        debtService.getTotalDebtByDate(credit, forDay)
            .flatMap(debt -> debt.getAccount(CreditCalculationConfiguration.PRINCIPAL))
            .filter(MonetaryAmount::isPositive)
            .ifPresent(amount -> plan.add(new AllocationPlanItem(
                credit.getId(),
                credit.getShortId(),
                "Principal balance",
                CreditCalculationConfiguration.GENERAL,
                amount
            )));
    }

    private boolean isActive(ExampleCredit c) {
        return c.getActualSnapshot() != null
            && c.getActualSnapshot().getStatus().in(CreditCalculationConfiguration.ACTIVE);
    }
}