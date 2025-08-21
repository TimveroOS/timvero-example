package com.timvero.example.portal.application;

import static com.timvero.example.admin.application.entity.ApplicationStatus.DECLINE;
import static com.timvero.example.admin.application.entity.ApplicationStatus.IN_UNDERWRITING;
import static com.timvero.example.admin.application.entity.ApplicationStatus.MANUAL_REVIEW;
import static com.timvero.example.admin.application.entity.ApplicationStatus.NEW;
import static com.timvero.example.admin.application.entity.ApplicationStatus.SERVICING;
import static com.timvero.example.admin.application.entity.ApplicationStatus.VOID;

import com.timvero.example.admin.application.entity.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Simplified application status for portal users", 
        enumAsRef = true)
public enum PortalApplicationStatus {

    @Schema(description = "Your application is being reviewed by our team")
    IN_PROCESS(List.of(NEW, MANUAL_REVIEW, IN_UNDERWRITING)),
    
    @Schema(description = "Your loan has been approved! Please sign the contract to proceed")
    PENDING_CONTRACT_SIGNATURE(List.of(ApplicationStatus.PENDING_CONTRACT_SIGNATURE)),
    
    @Schema(description = "Please select your preferred loan terms and conditions")
    CONDITION_CHOOSING(List.of(ApplicationStatus.CONDITION_CHOOSING)),
    
    @Schema(description = "Application has been cancelled")
    VOIDED(List.of(VOID)),
    
    @Schema(description = "Unfortunately, your application was not approved")
    DECLINED(List.of(DECLINE)),
    
    @Schema(description = "Your loan is active and payments are being processed")
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
