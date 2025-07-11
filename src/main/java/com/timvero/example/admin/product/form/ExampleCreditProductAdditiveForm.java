package com.timvero.example.admin.product.form;

import com.timvero.application.procuring.ProcuringType;
import com.timvero.loan.execution_result.ExecutionResultType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExampleCreditProductAdditiveForm {

    private Long additiveId;

    @NotNull
    private UUID product;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal minAmount;

    @NotNull
    private BigDecimal maxAmount;

    @NotNull
    private Integer minTerm;

    @NotNull
    private Integer maxTerm;

    @NotNull
    private BigDecimal interestRate;

    @NotEmpty
    private Map<ExecutionResultType, @NotNull UUID> offerEngines = new HashMap<>();

    @NotNull
    private ProcuringType procuringType;

    public Long getAdditiveId() {
        return additiveId;
    }

    public void setAdditiveId(Long additiveId) {
        this.additiveId = additiveId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getProduct() {
        return product;
    }

    public void setProduct(UUID product) {
        this.product = product;
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

    public Map<ExecutionResultType, UUID> getOfferEngines() {
        return offerEngines;
    }

    public void setOfferEngines(Map<ExecutionResultType, UUID> offerEngines) {
        this.offerEngines = offerEngines;
    }

    public ProcuringType getProcuringType() {
        return procuringType;
    }

    public void setProcuringType(ProcuringType procuringType) {
        this.procuringType = procuringType;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}

