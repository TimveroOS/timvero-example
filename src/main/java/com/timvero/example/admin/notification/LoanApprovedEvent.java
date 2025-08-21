package com.timvero.example.admin.notification;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.ApplicationStatus;
import com.timvero.structure.notification.execution.NotificationEvent;
import com.timvero.structure.notification.execution.NotificationEventType;
import com.timvero.structure.notification.execution.NotificationModel;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LoanApprovedEvent extends NotificationEvent<Application, NotificationModel> {

    public static final NotificationEventType TYPE = new NotificationEventType("LOAN_APPROVED");

    @Override
    public NotificationEventType getType() {
        return TYPE;
    }

    public boolean notify(UUID applicationId) {
        return super.notify(applicationId, new NotificationModel());
    }

    @Override
    protected boolean isSuitableTestEntity(Application application) {
        return application.getStatus() == ApplicationStatus.SERVICING;
    }
}
