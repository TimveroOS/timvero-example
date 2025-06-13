package com.timvero.example.admin.participant.action;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.ground.action.EntityAction;
import com.timvero.web.common.action.SimpleActionController;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/void")
public class VoidParticipantAction extends SimpleActionController<UUID, Participant> {

    @Override
    protected EntityAction<? super Participant, Object> action() {
        return when(p -> p.getStatus().isActive())
            .then((p, f, u) -> {
                p.setStatus(ParticipantStatus.VOID);
            });
    }
}
