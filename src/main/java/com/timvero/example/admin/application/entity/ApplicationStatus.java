package com.timvero.example.admin.application.entity;

public enum ApplicationStatus {
    NEW,
    MANUAL_REVIEW,
    IN_UNDERWRITING,
    CONDITION_CHOOSING,
    SECONDARY_MANUAL_REVIEW,
    IN_PROGRESS,
    PENDING_CONFIRM_CONDITION,
    PENDING_CONTRACT_SIGNATURE,
    SERVICING,
    DECLINE,
    VOID;
}
