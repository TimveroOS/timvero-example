package com.timvero.example.admin.notification;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.ApplicationStatus;
import com.timvero.example.admin.application.entity.Application_;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.ground.checker.CheckerListenerRegistry;
import com.timvero.ground.checker.EntityChecker;
import com.timvero.loan.decline_reason.entity.DeclineReason;
import com.timvero.loan.pending_decision.DecisionStatus;
import com.timvero.loan.pending_decision.PendingDecision;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * EntityChecker that automatically sends notifications when application status changes.
 *
 * This checker monitors Application entities for status changes and triggers
 * appropriate notifications when applications are approved or declined.
 * It integrates with the notification system to provide automated customer
 * communication without manual intervention.
 *
 * Monitored status changes:
 * <ul>
 *   <li>Any status → SERVICING: Sends loan approval notification</li>
 *   <li>Any status → DECLINE: Sends loan decline notification</li>
 * </ul>
 *
 * The checker runs asynchronously after the status change is committed to
 * ensure data consistency and avoid blocking the main transaction.
 */
@Component
public class ApplicationStatusNotificationChecker extends EntityChecker<Application, UUID> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStatusNotificationChecker.class);

    @Autowired
    private LoanApprovedEvent loanApprovedEvent;

    @Autowired
    private LoanDeclinedEvent loanDeclinedEvent;

    @Override
    protected void registerListeners(CheckerListenerRegistry<Application> registry) {
        registry.entityChange().updated(Application_.STATUS);
    }

    @Override
    protected boolean isAvailable(Application application) {
        ApplicationStatus currentStatus = application.getStatus();

        // Only process applications that are now in notification-worthy statuses
        boolean isNotificationStatus = currentStatus == ApplicationStatus.CONDITION_CHOOSING ||
                                     currentStatus == ApplicationStatus.DECLINE;

        if (isNotificationStatus) {
            logger.debug("Application {} status changed to {}, notification may be needed",
                application.getId(), currentStatus);
        }

        return isNotificationStatus;
    }

    @Override
    protected void perform(Application application) {
        ApplicationStatus status = application.getStatus();
        UUID applicationId = application.getId();

        logger.debug("Processing notification for application {} with status {}",
            applicationId, status);

        switch (status) {
            case CONDITION_CHOOSING -> {
                // Application approved - send approval notification
                logger.debug("Sending loan approval notification for application: {}", applicationId);
                loanApprovedEvent.notify(applicationId);
            }

            case DECLINE -> {
                // Application declined - send decline notification
                logger.debug("Sending loan decline notification for application: {}", applicationId);

                // Try to extract decline reason from application context or notes
                Participant participant = application.getBorrowerParticipant();
                List<PendingDecision> decisions = participant.getPendingDecisions();
                DeclineReason declineReason =
                    decisions.stream().filter(d -> d.getStatus() == DecisionStatus.DECLINED)
                        .map(PendingDecision::getDeclineReason).findAny().orElse(null);

                loanDeclinedEvent.notify(applicationId, declineReason, true);
            }

            default -> {
                // This shouldn't happen due to isAvailable() check, but handle gracefully
                logger.warn("Unexpected application status in notification checker: {} for application: {}",
                    status, applicationId);
            }
        }
    }
}