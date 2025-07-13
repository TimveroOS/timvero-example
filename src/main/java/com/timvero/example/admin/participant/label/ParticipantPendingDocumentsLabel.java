package com.timvero.example.admin.participant.label;

import com.timvero.example.admin.participant.checker.BorrowerStartTreeChecker;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.ground.entity_marker.label.EntityLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParticipantPendingDocumentsLabel implements EntityLabel<Participant> {
    @Autowired
    private BorrowerStartTreeChecker startTreeChecker;

    @Override
    public boolean isEntityMarked(Participant participant) {
        return startTreeChecker.needSignature(participant) && !startTreeChecker.hasSignature(participant);
    }

    @Override
    public String getName() {
        return "pendingDocuments";
    }
}
