package com.timvero.example.admin.participant.action;

import com.timvero.example.admin.participant.ParticipantService;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.ground.action.EntityAction;
import com.timvero.web.common.action.EntityActionController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/approve")
@Order(2000)
public class ApproveParticipantAction extends EntityActionController<UUID, Participant, Object> {

    private final ParticipantService participantService;

    public ApproveParticipantAction(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @Override
    public String getHighlighted() {
        return BTN_SUCCESS;
    }

    @Override
    protected EntityAction<? super Participant, Object> action() {
        return when(p -> p.getStatus().in(ParticipantStatus.MANUAL_APPROVAL) &&
            p.getPendingDecisions().isEmpty())
            .then((participant, f, user) -> {
                participantService.approve(participant.getId());
            });
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) {
        return "/participant/action/approve";
    }
}
