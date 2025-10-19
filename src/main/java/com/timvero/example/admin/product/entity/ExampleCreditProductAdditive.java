package com.timvero.example.admin.product.entity;

import static com.timvero.ground.hibernate.type.ColumnDefenition.NUMERIC;
import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_PRECISION;
import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_SCALE;

import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.loan.product.entity.CreditProductAdditive;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.math.BigDecimal;

@Entity
public class ExampleCreditProductAdditive extends CreditProductAdditive {

    @Column(nullable = false)
    private String name;

    @Column(precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal minAmount;

    @Column(precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal maxAmount;

    @Column(nullable = false)
    private Integer minTerm;

    @Column(nullable = false)
    private Integer maxTerm;

    @Column(nullable = false, columnDefinition = NUMERIC)
    private BigDecimal interestRate;

    public ExampleCreditProductAdditive() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getMinTerm() {
        return minTerm;
    }

    public void setMinTerm(Integer minTerm) {
        this.minTerm = minTerm;
    }

    public Integer getMaxTerm() {
        return maxTerm;
    }

    public void setMaxTerm(Integer maxTerm) {
        this.maxTerm = maxTerm;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    @Transient
    @Override
    public ExampleProductOffer createOffer() {
        return new ExampleProductOffer();
    }

    @Override
    public String toString() {
        return "Product Proposal #" + getId() + " ["
            + "minAmount=" + minAmount + ", "
            + "maxAmount=" + maxAmount + ", "
            + "minTerm=" + minTerm + ", "
            + "maxTerm=" + maxTerm + ", "
            + "procuring=" + getProcuringType() + "]";
    }

    @Override
    public String getDisplayedName() {
        return name;
    }
}
