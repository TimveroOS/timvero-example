package com.timvero.example.admin.credit.entity;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.ground.entity.NamedEntity;
import com.timvero.loan.covenantexecution.entity.HasCovenant;
import com.timvero.servicing.credit.entity.Credit;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@DiscriminatorValue("1")
public class ExampleCredit extends Credit implements NamedEntity, HasCovenant {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Application application;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "condition")
    @Fetch(FetchMode.JOIN)
    private ExampleCreditCondition condition;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public ExampleCreditCondition getCondition() {
        return condition;
    }

    public void setCondition(ExampleCreditCondition condition) {
        this.condition = condition;
    }

    @Override
    public String getDisplayedName() {
        return "Loan for " + getApplication().getBorrowerParticipant().getDisplayedName();
    }

    @Transient
    public LocalDate getMaturityDate() {
        return getStartDate().plus(getCondition().getPeriod().multipliedBy(getCondition().getTerm()));
    }

    @Transient
    public String getShortId() {
        return getId().toString().substring(0, 6);
    }
}