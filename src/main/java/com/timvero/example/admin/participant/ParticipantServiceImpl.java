package com.timvero.example.admin.participant;

import static com.timvero.example.admin.participant.entity.ParticipantStatus.APPROVED;
import static com.timvero.example.admin.participant.entity.ParticipantStatus.DECLINED;
import static com.timvero.example.admin.participant.entity.ParticipantStatus.IN_PROCESS;
import static com.timvero.example.admin.participant.entity.ParticipantStatus.MANUAL_APPROVAL;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository repository;

    public ParticipantServiceImpl(ParticipantRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void manualApproval(UUID participantId) {
        setStatus(MANUAL_APPROVAL, participantId, IN_PROCESS);
    }

    @Override
    @Transactional
    public void approve(UUID participantId) {
        setStatus(APPROVED, participantId, IN_PROCESS, MANUAL_APPROVAL);
    }

    @Override
    @Transactional
    public void decline(UUID participantId) {
        setStatus(DECLINED, participantId, IN_PROCESS, MANUAL_APPROVAL);
    }

    protected void setStatus(ParticipantStatus newStatus, UUID participantId, ParticipantStatus... acceptablePrevious) {
        final Participant participant = repository.getSync(participantId);
        final ParticipantStatus currentStatus = participant.getStatus();

        if (isStatusChangeAllowed(currentStatus, acceptablePrevious)) {
            participant.setStatus(newStatus);
        } else {
            validateStatusTransition(newStatus, participant, currentStatus, acceptablePrevious);
        }
    }

    private boolean isStatusChangeAllowed(ParticipantStatus currentStatus, ParticipantStatus... acceptablePrevious) {
        return acceptablePrevious.length == 0 || currentStatus.in(acceptablePrevious);
    }

    private void validateStatusTransition(ParticipantStatus newStatus, Participant participant,
        ParticipantStatus currentStatus, ParticipantStatus... acceptablePrevious) {
        String errorMessage = String.format(
            "Status transition failed for Participant %s: cannot change from '%s' to '%s'. Expected previous status: %s",
            participant.getId(), currentStatus, newStatus, Arrays.toString(acceptablePrevious)
        );

        Assert.state(newStatus == currentStatus, errorMessage);
    }
}