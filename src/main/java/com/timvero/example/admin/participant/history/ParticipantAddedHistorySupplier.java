package com.timvero.example.admin.participant.history;

import com.timvero.base.audit.impl.AbstractAddedHistorySupplier;
import com.timvero.example.admin.participant.entity.Participant;
import java.util.UUID;
import org.springframework.stereotype.Component;

// tag::class[]
@Component
public class ParticipantAddedHistorySupplier extends AbstractAddedHistorySupplier<UUID, Participant> {

    public static final String PARTICIPANT_CREATED = "PARTICIPANT_CREATED";

    public ParticipantAddedHistorySupplier() {
        super(Participant.class, PARTICIPANT_CREATED);
    }
}
// end::class[]
