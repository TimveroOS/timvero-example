package com.timvero.example.portal.application;

import static com.timvero.example.admin.application.entity.ApplicationStatus.DECLINE;
import static com.timvero.example.admin.application.entity.ApplicationStatus.IN_UNDERWRITING;
import static com.timvero.example.admin.application.entity.ApplicationStatus.MANUAL_REVIEW;
import static com.timvero.example.admin.application.entity.ApplicationStatus.NEW;
import static com.timvero.example.admin.application.entity.ApplicationStatus.SERVICING;
import static com.timvero.example.admin.application.entity.ApplicationStatus.VOID;

import com.timvero.example.admin.application.entity.ApplicationStatus;
import java.util.List;

public enum PortalApplicationStatus {

    IN_PROCESS(List.of(NEW, MANUAL_REVIEW, IN_UNDERWRITING)),
    PENDING_CONTRACT_SIGNATURE(List.of(ApplicationStatus.PENDING_CONTRACT_SIGNATURE)),
    CONDITION_CHOOSING(List.of(ApplicationStatus.CONDITION_CHOOSING)),
    VOIDED(List.of(VOID)),
    DECLINED(List.of(DECLINE)),
    ACTIVE(List.of(SERVICING));

    private final List<ApplicationStatus> APPLICATION_STATUSES;

    PortalApplicationStatus(List<ApplicationStatus> statuses) {
        APPLICATION_STATUSES = statuses;
    }

    public static PortalApplicationStatus fromApplicationStatus(ApplicationStatus applicationStatus) {
        for (PortalApplicationStatus portalStatus : values()) {
            if (portalStatus.APPLICATION_STATUSES.contains(applicationStatus)) {
                return portalStatus;
            }
        }
        return null;
    }
}