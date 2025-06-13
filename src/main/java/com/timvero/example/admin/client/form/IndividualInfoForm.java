package com.timvero.example.admin.client.form;

import com.timvero.common.validation.ValidationUtils;
import com.timvero.example.admin.client.entity.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class IndividualInfoForm {

    // tag::individual-info-form[]
    @NotBlank
    private String nationalId;

    @NotBlank
    private String fullName;

    @PastOrPresent
    @DateTimeFormat(pattern = ValidationUtils.PATTERN_DATEPICKER_FORMAT)
    private LocalDate dateOfBirth;

    @NotNull
    private Country residenceCountry;
    // end::individual-info-form[]

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Country getResidenceCountry() {
        return residenceCountry;
    }

    public void setResidenceCountry(Country residenceCountry) {
        this.residenceCountry = residenceCountry;
    }
}
