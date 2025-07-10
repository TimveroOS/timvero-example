package com.timvero.example.admin.product.form;

import static com.timvero.ground.tools.Messages.NUMBER;
import static com.timvero.ground.tools.Messages.POSITIVE_NUMBER;

import com.timvero.example.admin.application.entity.ApplicationType;
import com.timvero.loan.execution_result.ExecutionResultType;
import com.timvero.loan.product.form.BaseCreditProductForm;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.money.CurrencyUnit;

public class CreditProductForm extends BaseCreditProductForm {

    @NotNull
    private ApplicationType applicationType;

    @NotNull
    private CurrencyUnit currency;

    @NotNull
    @Digits(integer = 15, fraction = 4, message = NUMBER)
    @DecimalMin(value = "0.0000", message = POSITIVE_NUMBER)
    private BigDecimal minAmount;

    @NotNull
    @Digits(integer = 15, fraction = 4, message = NUMBER)
    @DecimalMin(value = "0.0000", message = POSITIVE_NUMBER)
    private BigDecimal maxAmount;

    @NotNull
    private Integer minTerm;

    @NotNull
    private Integer maxTerm;

    @NotEmpty
    private Map<ExecutionResultType, @NotNull UUID> offerEngines = new HashMap<>();

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
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

    public Map<ExecutionResultType, UUID> getOfferEngines() {
        return offerEngines;
    }

    public void setOfferEngines(Map<ExecutionResultType, UUID> offerEngines) {
        this.offerEngines = offerEngines;
    }
}
