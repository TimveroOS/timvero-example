package com.timvero.example.admin.participant.entity;

import com.timvero.ground.entity.enums.InEnum;

// tag::enum[]
public enum ParticipantStatus implements InEnum<ParticipantStatus> {
    NEW,
    IN_PROCESS,
    MANUAL_APPROVAL,
    APPROVED,
    DECLINED,
    VOID;

    public boolean isActive() {
        return !this.in(DECLINED, VOID);
    }
}
// end::enum[]
