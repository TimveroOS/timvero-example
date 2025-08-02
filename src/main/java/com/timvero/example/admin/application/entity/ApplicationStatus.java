package com.timvero.example.admin.application.entity;

import com.timvero.ground.entity.enums.InEnum;

public enum ApplicationStatus implements InEnum<ApplicationStatus> {
    NEW,
    MANUAL_REVIEW,
    IN_UNDERWRITING,
    CONDITION_CHOOSING,
    PENDING_CONTRACT_SIGNATURE,
    SERVICING,
    DECLINE,
    VOID;
}
