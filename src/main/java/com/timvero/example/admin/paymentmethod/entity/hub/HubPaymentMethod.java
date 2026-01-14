package com.timvero.example.admin.paymentmethod.entity.hub;

import com.timvero.transfer.method.entity.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
// tag::entity[]
@Entity
@DiscriminatorValue(HubPaymentMethod.TYPE)
public class HubPaymentMethod extends PaymentMethod {

    // end::entity[]

    public static final String TYPE = "HUB";

    @Column(name = "document_number")
    private String documentNumber;

    protected HubPaymentMethod() {
        super(TYPE);
    }

    public HubPaymentMethod(String documentNumber) {
        super(TYPE);
        this.documentNumber = documentNumber;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

}
