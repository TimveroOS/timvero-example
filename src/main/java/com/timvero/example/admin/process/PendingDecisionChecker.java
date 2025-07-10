package com.timvero.example.admin.process;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.ground.checker.CheckerListenerRegistry;
import com.timvero.ground.checker.EntityChecker;
import com.timvero.ground.util.TransactionUtils;
import com.timvero.loan.pending_decision.DecisionStatus;
import com.timvero.loan.pending_decision.PendingDecision;
import com.timvero.loan.pending_decision.PendingDecision_;
import org.springframework.stereotype.Component;

@Component
public class PendingDecisionChecker extends EntityChecker<PendingDecision, Long> {

    private final ParticipantRepository participantRepository;
    private final FinishedScoringListener finishedScoringListener;

    public PendingDecisionChecker(ParticipantRepository participantRepository,
        FinishedScoringListener finishedScoringListener) {
        this.participantRepository = participantRepository;
        this.finishedScoringListener = finishedScoringListener;
    }

    @Override
    protected void registerListeners(CheckerListenerRegistry<PendingDecision> registry) {
        registry.entityChange().updated(PendingDecision_.STATUS);
    }

    @Override
    protected boolean isAvailable(PendingDecision decision) {
        return decision.getStatus() == DecisionStatus.APPROVED || decision.getStatus() == DecisionStatus.DECLINED;
    }

    @Override
    protected void perform(PendingDecision decision) {
        Long holderId = decision.getHolder().getId();

        Participant participant = participantRepository.findByPendingDecisionHolderId(holderId)
            .orElseThrow(() -> new RuntimeException("No target entity with holder id " + holderId));
        TransactionUtils.afterTransaction(() -> finishedScoringListener.process(participant.getId()));
    }
}
