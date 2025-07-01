package com.timvero.example.admin.document.signature;

import com.timvero.ground.document.signable.DocumentSignature;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "physical_document_signature")
@DiscriminatorValue("PHYSICAL")
public class PhysicalDocumentSignature extends DocumentSignature {

}
