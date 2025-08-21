package com.timvero.example.portal.application.form;

import com.timvero.example.admin.participant.entity.Employment;
import com.timvero.example.admin.participant.entity.Periodicity;
import jakarta.validation.constraints.NotNull;
import javax.money.MonetaryAmount;
import java.util.UUID;

public class CreateApplicationRequest {

    @NotNull
    private UUID clientId;
    
    @NotNull
    private Employment employment;

    @NotNull
    private Periodicity howOftenIncomeIsPaid;

    @NotNull
    private MonetaryAmount totalAnnualIncome;

    private MonetaryAmount monthlyOutgoings;

    private String githubUsername;

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public Employment getEmployment() {
        return employment;
    }

    public void setEmployment(Employment employment) {
        this.employment = employment;
    }

    public Periodicity getHowOftenIncomeIsPaid() {
        return howOftenIncomeIsPaid;
    }

    public void setHowOftenIncomeIsPaid(Periodicity howOftenIncomeIsPaid) {
        this.howOftenIncomeIsPaid = howOftenIncomeIsPaid;
    }

    public MonetaryAmount getTotalAnnualIncome() {
        return totalAnnualIncome;
    }

    public void setTotalAnnualIncome(MonetaryAmount totalAnnualIncome) {
        this.totalAnnualIncome = totalAnnualIncome;
    }

    public MonetaryAmount getMonthlyOutgoings() {
        return monthlyOutgoings;
    }

    public void setMonthlyOutgoings(MonetaryAmount monthlyOutgoings) {
        this.monthlyOutgoings = monthlyOutgoings;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }
}
