package com.timvero.example.admin.participant;

import java.util.UUID;

public interface ParticipantService {

    void manualApproval(UUID participantId);

    void approve(UUID participantId);

    void decline(UUID participantId);
}
