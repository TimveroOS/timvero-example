package com.timvero.example.admin.client.history;

import com.timvero.base.audit.impl.AbstractRelatedEntityAddedHistorySupplier;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRole;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

// tag::class[]
@Component
public class ClientParticipantCreatedEventSupplier
    extends AbstractRelatedEntityAddedHistorySupplier<Client, UUID, Participant> {

    public static final String CLIENT_PARTICIPANT_CREATED = "CLIENT_PARTICIPANT_CREATED";

    public ClientParticipantCreatedEventSupplier() {
        super(
            Client.class,
            Participant.class,
            CLIENT_PARTICIPANT_CREATED,
            "/history/fragments/client/participant_created"
        );
    }

    @Override
    protected List<Participant> getRelatedEntities(Client client) {
        return client.getParticipants().stream()
            .filter(participant -> participant.getRoles().contains(ParticipantRole.BORROWER))
            .toList();
    }
}
// end::class[]
