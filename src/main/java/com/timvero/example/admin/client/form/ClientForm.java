package com.timvero.example.admin.client.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class ClientForm {

    // tag::client-form[]
    @Valid
    @NotNull
    private IndividualInfoForm individualInfo;

    @Valid
    @NotNull
    private ContactInfoForm contactInfo;
    // end::client-form[]

    public IndividualInfoForm getIndividualInfo() {
        return individualInfo;
    }

    public void setIndividualInfo(IndividualInfoForm individualInfo) {
        this.individualInfo = individualInfo;
    }

    public ContactInfoForm getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfoForm contactInfo) {
        this.contactInfo = contactInfo;
    }
}
