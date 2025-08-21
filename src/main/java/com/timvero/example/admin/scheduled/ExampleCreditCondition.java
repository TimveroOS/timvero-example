package com.timvero.example.admin.scheduled;

import static com.timvero.ground.hibernate.type.ColumnDefenition.NUMERIC;

import com.timvero.base.entity.UUIDPersistable;
import com.timvero.example.admin.offer.entity.ExampleSecuredOffer;
import com.timvero.scheduled.entity.CreditCondition;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Period;
import javax.money.MonetaryAmount;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "credit_condition")
@Immutable
public class ExampleCreditCondition extends UUIDPersistable implements CreditCondition {

    @Embedded
    private MonetaryAmount principal;

    @Column(nullable = false)
    private String engineName;

    @Column(nullable = false, columnDefinition = NUMERIC)
    private BigDecimal interestRate;

    @Column(nullable = false, columnDefinition = NUMERIC)
    private BigDecimal lateFeeRate;

    @Column(nullable = false)
    private String dayCountMethod;

    @Column(nullable = false)
    private Period period;

    @Column(nullable = false)
    private Integer term;

    @Embedded
    @NotNull
    private MonetaryAmount regularPayment;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private ExampleSecuredOffer securedOffer;

    protected ExampleCreditCondition() {}

    public ExampleCreditCondition(MonetaryAmount principal, String engineName, BigDecimal interestRate,
        BigDecimal lateFeeRate, String dayCountMethod, Period period, Integer term, MonetaryAmount regularPayment,
        ExampleSecuredOffer securedOffer) {
        super();
        this.principal = principal;
        this.engineName = engineName;
        this.interestRate = interestRate;
        this.lateFeeRate = lateFeeRate;
        this.dayCountMethod = dayCountMethod;
        this.period = period;
        this.term = term;
        this.regularPayment = regularPayment;
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

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public BigDecimal getLateFeeRate() {
        return lateFeeRate;
    }

    public String getDayCountMethod() {
        return dayCountMethod;
    }

    public Period getPeriod() {
        return period;
    }

    public Integer getTerm() {
        return term;
    }

    public MonetaryAmount getRegularPayment() {
        return regularPayment;
    }

    public ExampleSecuredOffer getSecuredOffer() {
        return securedOffer;
    }
}
