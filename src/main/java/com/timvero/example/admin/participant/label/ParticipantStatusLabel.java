package com.timvero.example.admin.participant.label;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.ground.entity_marker.label.EntityStatusLabel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ParticipantStatusLabel extends EntityStatusLabel<Participant> {

    public ParticipantStatusLabel() {
        super(Participant::getStatus);
    }
}
