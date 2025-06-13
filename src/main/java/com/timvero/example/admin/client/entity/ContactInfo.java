package com.timvero.example.admin.client.entity;

import static com.timvero.common.validation.ValidationUtils.PHONE_PATTERN;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Embeddable
@Audited
public class ContactInfo {

    @Column(nullable = false)
    @Email
    @KeywordField
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = PHONE_PATTERN)
    @KeywordField
    private String phone;

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
