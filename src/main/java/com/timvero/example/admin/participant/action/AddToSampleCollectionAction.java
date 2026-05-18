package com.timvero.example.admin.participant.action;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.risk.sample.participant.ParticipantResponseSampleManager;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.entity.reload.ReloadPageHelper;
import com.timvero.web.common.action.SimpleActionController;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/add-to-sample-collection")
public class AddToSampleCollectionAction extends SimpleActionController<UUID, Participant> {

    @Autowired
    private ReloadPageHelper reloadPageHelper;

    @Autowired
    public ParticipantResponseSampleManager sampleManager;

    @Override
    protected EntityAction<? super Participant, Object> action() {
        return when(p -> p.getClient() != null && !isInCollection(p))
            .then((participant, form, user) -> {
                sampleManager.create(participant);
                reloadPageHelper.reload(participant);
            });
    }

    private boolean isInCollection(Participant participant) {
        return sampleManager.isPresent(participant);
    }
}
