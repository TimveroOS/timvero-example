package com.timvero.example.admin.participant.label;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.ground.entity_marker.label.EntityLabel;
import org.springframework.stereotype.Component;

@Component
public class ParticipantOffersGenerationErrorLabel implements EntityLabel<Participant> {

    @Override
    public boolean isEntityMarked(Participant participant) {
        return participant.getOfferGenerationException() != null;
    }

    @Override
    public String getName() {
        return "offersGenerationErrorParticipant";
    }
}
