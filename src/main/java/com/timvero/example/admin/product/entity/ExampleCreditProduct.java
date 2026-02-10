package com.timvero.example.admin.product.entity;

import static com.timvero.ground.hibernate.type.ColumnDefenition.NUMERIC;
import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_PRECISION;
import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_SCALE;

import com.timvero.loan.product.entity.CreditProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import javax.money.CurrencyUnit;

@Entity
public class ExampleCreditProduct extends CreditProduct {

    @Column(updatable = false)
    private CurrencyUnit currency;

    @Column(precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal minAmount;

    @Column(precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal maxAmount;

    @Column(nullable = false)
    private Integer minTerm;

    @Column(nullable = false)
    private Integer maxTerm;

    @Column(nullable = false, columnDefinition = NUMERIC)
    private BigDecimal lateFeeRate;

    @Column(nullable = false)
    private String dayCountMethod;

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

    public BigDecimal getLateFeeRate() {
        return lateFeeRate;
    }

    public void setLateFeeRate(BigDecimal lateFeeRate) {
        this.lateFeeRate = lateFeeRate;
    }

    public String getDayCountMethod() {
        return dayCountMethod;
    }

    public void setDayCountMethod(String dayCountMethod) {
        this.dayCountMethod = dayCountMethod;
    }
}
