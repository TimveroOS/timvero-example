package com.timvero.example.admin.participant.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;

import com.timvero.base.entity.AbstractAuditable;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.risk.github.GithubDataSourceSubject;
import com.timvero.flowable.internal.execution.ProcessEntity;
import com.timvero.ground.document.HasDocuments;
import com.timvero.ground.entity.NamedEntity;
import com.timvero.integration.docusign.DocusignSigner;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

// tag::entity[]
@Entity
@Table
@Audited
@Indexed
public class Participant extends AbstractAuditable<UUID> implements NamedEntity, GithubDataSourceSubject, HasDocuments, ProcessEntity, DocusignSigner {

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ParticipantStatus status = ParticipantStatus.NEW;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<ParticipantRole> roles;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(nullable = false, updatable = false)
    private Application application;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(nullable = false, updatable = false)
    private Client client;

    @Column(nullable = false)
    @Enumerated(STRING)
    private Employment employment;

    @Column(nullable = false)
    @Enumerated(STRING)
    private Periodicity howOftenIncomeIsPaid;

    @Embedded
    private MonetaryAmount totalAnnualIncome;

    @Embedded
    private MonetaryAmount monthlyOutgoings;

// end::entity[]

    @Column
    private String githubUsername;

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public Set<ParticipantRole> getRoles() {
        return roles != null ? roles : (roles = new HashSet<>());
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    @Override
    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    @Override
    public String getDisplayedName() {
        return getClient().getDisplayedName();
    }

    @Transient
    @Override
    public String getPrimaryId() {
        return getClient().getIndividualInfo().getNationalId();
    }

    @Transient
    @Override
    public String getSignerName() {
        return getClient().getIndividualInfo().getFullName();
    }

    @Override
    public String getSignerEmail() {
        return getClient().getContactInfo().getEmail();
    }
}
