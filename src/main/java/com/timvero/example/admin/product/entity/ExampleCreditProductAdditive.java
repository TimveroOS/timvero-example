package com.timvero.example.admin.product.entity;

import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_PRECISION;
import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_SCALE;

import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.loan.product.entity.CreditProduct;
import com.timvero.loan.product.entity.CreditProductAdditive;
import com.timvero.servicing.AmountConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.math.BigDecimal;

@Entity
public class ExampleCreditProductAdditive extends CreditProductAdditive {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "min_amount", precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal maxAmount;

    @Column(name = "min_term", nullable = false)
    private Integer minTerm;

    @Column(name = "max_term", nullable = false)
    private Integer maxTerm;

    @Column(name = "interest_rate", precision = 19, scale = AmountConstants.PERCENT_SCALE, nullable = false)
    private BigDecimal interestRate;

    protected ExampleCreditProductAdditive() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExampleCreditProductAdditive(CreditProduct product) {
        super(product);
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

/*    @Override
    public String toString() {
        return "Product Proposal #" + getId() + " ["
            + "minAmount=" + getMinAmount() + ", "
            + "maxAmount=" + getMaxAmount() + ", "
            + "minTerm=" + getMinTerm() + ", "
            + "maxTerm=" + getMaxTerm() + ", "
            + "procuring=" + getProcuringType() + "]";
    }*/

    @Override
    public String getDisplayedName() {
        return name;
    }
}
