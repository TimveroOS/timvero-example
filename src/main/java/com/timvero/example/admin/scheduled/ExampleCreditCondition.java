package com.timvero.example.admin.scheduled;

import com.timvero.example.admin.offer.entity.ExampleSecuredOffer;
import com.timvero.ground.hibernate.type.ColumnDefenition;
import com.timvero.scheduled.entity.CreditCondition;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Period;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.hibernate.annotations.Immutable;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "credit_condition")
@Immutable
public class ExampleCreditCondition extends AbstractPersistable<UUID> implements CreditCondition {

    @Embedded
    private MonetaryAmount principal;

    @Column(nullable = false)
    private String engineName;

    @Column(nullable = false, columnDefinition = ColumnDefenition.NUMERIC)
    private BigDecimal annualInterest;

    @Column(nullable = false)
    private Period period = Period.ofMonths(1);

    @Column(nullable = false)
    private Integer term;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private ExampleSecuredOffer securedOffer;

    protected ExampleCreditCondition() {}

    public ExampleCreditCondition(MonetaryAmount principal, String engineName, BigDecimal annualInterest,
        Integer term, ExampleSecuredOffer securedOffer) {
        super();
        this.principal = principal;
        this.engineName = engineName;
        this.annualInterest = annualInterest;
        this.term = term;
        this.securedOffer = securedOffer;
    }

    @Override
    public MonetaryAmount getPrincipal() {
        return principal;
    }

    @Override
    public String getScheduleEngineName() {
        return engineName;
    }

    public BigDecimal getAnnualInterest() {
        return annualInterest;
    }

    public Period getPeriod() {
        return period;
    }

    public Integer getTerm() {
        return term;
    }

    public ExampleSecuredOffer getSecuredOffer() {
        return securedOffer;
    }
}
