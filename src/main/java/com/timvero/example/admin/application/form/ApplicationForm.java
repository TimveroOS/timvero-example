package com.timvero.example.admin.application.form;

import com.timvero.example.admin.participant.form.ParticipantForm;
import jakarta.validation.Valid;

public class ApplicationForm {

    // tag::application-form[]
    @Valid
    private ParticipantForm borrowerParticipant;
    // end::application-form[]

    public ParticipantForm getBorrowerParticipant() {
        return borrowerParticipant;
    }

    public void setBorrowerParticipant(ParticipantForm borrowerParticipant) {
        this.borrowerParticipant = borrowerParticipant;
    }
}
