package com.timvero.example.admin.notification;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.structure.notification.entity.NotificationTemplate;
import com.timvero.structure.notification.execution.NotificationEventType;
import com.timvero.structure.notification.execution.NotificationRecipientType;
import com.timvero.structure.notification.execution.resolver.NotificationRecipientResolver;
import java.util.List;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(10)
public class ClientNotificationRecipientResolver implements NotificationRecipientResolver {

    public static final NotificationRecipientType TYPE = new NotificationRecipientType("CLIENT");

    @Override
    public NotificationRecipientType getType() {
        return TYPE;
    }

    @Override
    public boolean supports(Class<?> entityType, NotificationEventType eventType) {
        return Application.class.isAssignableFrom(entityType);
    }

    @Override
    public Iterable<Client> resolve(Object entity, NotificationTemplate template) {
        Application application = (Application) entity;
        return List.of(application.getBorrowerParticipant().getClient());
    }
}