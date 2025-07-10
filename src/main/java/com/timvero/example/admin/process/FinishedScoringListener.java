package com.timvero.example.admin.process;

import com.timvero.example.admin.participant.ParticipantService;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.flowable.internal.service.FinishedScoringEvent;
import com.timvero.ground.event.EntityEventListener;
import com.timvero.loan.pending_decision.DecisionStatus;
import com.timvero.loan.pending_decision.PendingDecision;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FinishedScoringListener implements EntityEventListener<FinishedScoringEvent<Participant>> {

    private final ParticipantService participantService;
    private final ParticipantRepository participantRepository;

    public FinishedScoringListener(ParticipantService participantService, ParticipantRepository participantRepository) {
        this.participantService = participantService;
        this.participantRepository = participantRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(FinishedScoringEvent<Participant> event) {
        process(event.getEntityId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(UUID participantId) {
        Participant participant = participantRepository.getSync(participantId);
        List<PendingDecision> decisions = participant.getPendingDecisions();

        boolean hasDeclined = decisions.stream().anyMatch(d -> d.getStatus() == DecisionStatus.DECLINED);
        boolean hasPending = decisions.stream().anyMatch(d -> d.getStatus() == DecisionStatus.PENDING);

        if (hasDeclined) {
            participantService.decline(participant.getId());
            decisions.stream()
                .filter(d -> d.getStatus() == DecisionStatus.PENDING)
                .forEach(d -> d.setStatus(DecisionStatus.NOT_ACTUAL));
        } else if (hasPending) {
            participantService.manualApproval(participant.getId());
        } else {
            participantService.approve(participant.getId());
        }
    }

}
