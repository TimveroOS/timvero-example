package com.timvero.example.admin.participant.action;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.loan.pending_decision.action.AssignToMeAction;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/assign-to-me")
public class AssignToMeParticipantAction extends AssignToMeAction<UUID, Participant> {
}
