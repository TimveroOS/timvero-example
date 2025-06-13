package com.timvero.example.admin.participant.form;

import com.timvero.example.admin.participant.entity.Employment;
import com.timvero.example.admin.participant.entity.Periodicity;
import jakarta.validation.constraints.NotNull;
import javax.money.MonetaryAmount;

public class ParticipantForm {

    // tag::participant-form[]
    @NotNull
    private Employment employment;

    @NotNull
    private Periodicity howOftenIncomeIsPaid;

    @NotNull
    private MonetaryAmount totalAnnualIncome;

    private MonetaryAmount monthlyOutgoings;
    // end::participant-form[]

    private String githubUsername;

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
