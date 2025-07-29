package com.timvero.example.portal.application.form;

import java.util.UUID;

public class CreateApplicationResponse {
    private UUID applicationId;

    public CreateApplicationResponse() {
    }

    public CreateApplicationResponse(UUID applicationId) {
        this.applicationId = applicationId;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }
}