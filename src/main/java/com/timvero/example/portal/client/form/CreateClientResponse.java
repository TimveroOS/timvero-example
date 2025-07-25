package com.timvero.example.portal.client.form;

import java.util.UUID;

public class CreateClientResponse {
    private UUID clientId;

    public CreateClientResponse(UUID clientId) {
        super();
        this.clientId = clientId;
    }

    public UUID getClientId() {
        return clientId;
    }
}
