package com.timvero.example.admin.offer.form;

import com.timvero.common.validation.ValidationUtils;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import javax.money.MonetaryAmount;
import org.springframework.format.annotation.DateTimeFormat;

public class ConditionForm {

    @NotBlank
    private String securedOfferKey;

    @Positive
    @NotNull
    private MonetaryAmount principal;

    @Min(1)
    @NotNull
    private Integer term;

    @DateTimeFormat(pattern = ValidationUtils.PATTERN_DATEPICKER_FORMAT)
    @NotNull
    private LocalDate start;

    public ConditionForm() {
    }

    public String getSecuredOfferKey() {
        return securedOfferKey;
    }

    public void setSecuredOfferKey(String securedOfferKey) {
        this.securedOfferKey = securedOfferKey;
    }

    public MonetaryAmount getPrincipal() {
        return principal;
    }

    public void setPrincipal(MonetaryAmount principal) {
        this.principal = principal;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }
}
