package com.timvero.example.admin.product.entity;

import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_PRECISION;
import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_SCALE;

import com.timvero.loan.product.entity.CreditProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import javax.money.CurrencyUnit;

@Entity
public class ExampleCreditProduct extends CreditProduct {

    @Column(name = "currency", updatable = false)
    private CurrencyUnit currency;

    @Column(name = "min_amount", precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal maxAmount;

    @Column(name = "min_term", nullable = false)
    private Integer minTerm;

    @Column(name = "max_term", nullable = false)
    private Integer maxTerm;

    public ExampleCreditProduct() {
        super();
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

    public CurrencyUnit getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyUnit currency) {
        this.currency = currency;
    }

}
