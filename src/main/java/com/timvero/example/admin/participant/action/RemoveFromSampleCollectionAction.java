package com.timvero.example.admin.participant.action;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.risk.sample.participant.ParticipantResponseSampleManager;
import com.timvero.ground.action.EntityAction;
import com.timvero.web.common.action.SimpleActionController;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/remove-from-sample-collection")
public class RemoveFromSampleCollectionAction extends SimpleActionController<UUID, Participant> {

    @Autowired
    public ParticipantResponseSampleManager sampleManager;

    @Override
    protected EntityAction<? super Participant, Object> action() {
        return when(this::isInCollection)
            .then((participant, form, user) -> sampleManager.remove(participant));
    }

    private boolean isInCollection(Participant participant) {
        return sampleManager.isPresent(participant);
    }
}
