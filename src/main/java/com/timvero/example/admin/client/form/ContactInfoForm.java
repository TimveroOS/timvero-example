package com.timvero.example.admin.client.form;

import com.timvero.common.validation.annotation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ContactInfoForm {

    // tag::contact-info-form[]
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Phone
    private String phone;
    // end::contact-info-form[]

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
