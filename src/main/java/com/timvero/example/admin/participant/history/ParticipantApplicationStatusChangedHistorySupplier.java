package com.timvero.example.admin.participant.history;

import com.timvero.base.audit.impl.AbstractRelatedEntityModifiedHistorySupplier;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.Application_;
import com.timvero.example.admin.participant.entity.Participant;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

// tag::class[]
@Component
public class ParticipantApplicationStatusChangedHistorySupplier
    extends AbstractRelatedEntityModifiedHistorySupplier<UUID, Participant, UUID, Application> {

    public static final String APPLICATION_STATUS_CHANGED = "APPLICATION_STATUS_CHANGED";

    public ParticipantApplicationStatusChangedHistorySupplier() {
        super(Participant.class, Application.class, APPLICATION_STATUS_CHANGED);
    }

    @Override
    protected Set<String> getTrackedFields() {
        return Set.of(Application_.STATUS);
    }

    @Override
    protected Application getRelatedEntity(Participant participant) {
        return participant.getApplication();
    }
}
// end::class[]
