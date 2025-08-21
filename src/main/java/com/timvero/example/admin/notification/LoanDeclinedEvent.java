package com.timvero.example.admin.notification;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.ApplicationStatus;
import com.timvero.loan.decline_reason.entity.DeclineReason;
import com.timvero.structure.notification.execution.NotificationEvent;
import com.timvero.structure.notification.execution.NotificationEventType;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LoanDeclinedEvent extends NotificationEvent<Application, LoanDeclineTemplateModel> {

    public static final NotificationEventType TYPE = new NotificationEventType("LOAN_DECLINED");

    @Override
    public NotificationEventType getType() {
        return TYPE;
    }

    public boolean notify(UUID applicationId, DeclineReason declineReason, boolean reapplicationEligible) {
        LoanDeclineTemplateModel model = new LoanDeclineTemplateModel();
        model.setDeclineReason(declineReason);
        model.setReapplicationEligible(reapplicationEligible);
        return super.notify(applicationId, model);
    }

    @Override
    protected boolean isSuitableTestEntity(Application application) {
        return application.getStatus().in(ApplicationStatus.CONDITION_CHOOSING,
            ApplicationStatus.PENDING_CONTRACT_SIGNATURE,
            ApplicationStatus.SERVICING);
    }
}
