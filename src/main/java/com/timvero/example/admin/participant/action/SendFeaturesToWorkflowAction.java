package com.timvero.example.admin.participant.action;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.flowable.internal.feature.action.AbstractSendFeaturesToWorkflowAction;
import com.timvero.structure.user.entity.UserAccount;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/send-features-to-workflow")
@Order(4950)
public class SendFeaturesToWorkflowAction extends AbstractSendFeaturesToWorkflowAction<Participant> {

    @Override
    protected boolean isAvailable(Participant participant, UserAccount user) {
        return !participant.getStatus().in(ParticipantStatus.NEW, ParticipantStatus.IN_PROCESS);
    }
}