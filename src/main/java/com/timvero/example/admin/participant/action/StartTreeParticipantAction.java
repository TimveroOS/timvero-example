package com.timvero.example.admin.participant.action;

import static com.timvero.example.admin.CustomConfiguration.PARTICIPANT_TREE;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.flowable.internal.service.DecisionProcessStarter;
import com.timvero.flowable.internal.service.ProcessExecutionService;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.entity.reload.ReloadPageHelper;
import com.timvero.web.common.action.SimpleActionController;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/start-tree")
@Controller
public class StartTreeParticipantAction extends SimpleActionController<UUID, Participant> {

    @Autowired
    private DecisionProcessStarter decisionTask;

    @Autowired
    private ProcessExecutionService processExecutionService;

    @Autowired
    private ReloadPageHelper pageHelper;

    @Override
    protected EntityAction<? super Participant, Object> action() {
        return when(participant ->
            participant.getStatus().in(ParticipantStatus.IN_PROCESS)
                && processExecutionService.isLastRealExecutionFailed(participant, PARTICIPANT_TREE))
            .then((participant, f, u) -> {
                decisionTask.start(PARTICIPANT_TREE, participant.getId());
                pageHelper.reload(participant);
            });
    }
}
