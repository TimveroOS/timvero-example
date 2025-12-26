package com.timvero.example.admin.client.history;

import com.timvero.base.audit.impl.AbstractModifiedHistorySupplier;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.client.entity.Client_;
import com.timvero.example.admin.client.entity.ContactInfo_;
import com.timvero.example.admin.client.entity.IndividualInfo_;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

// tag::class[]
@Component
public class ClientModifiedHistorySupplier extends AbstractModifiedHistorySupplier<UUID, Client> {

    public static final String CLIENT_MODIFIED = "CLIENT_MODIFIED";

    public ClientModifiedHistorySupplier() {
        super(Client.class, CLIENT_MODIFIED);
    }

    @Override
    protected Set<String> getTrackedFields() {
        return Set.of(
            Client_.INDIVIDUAL_INFO + "." + IndividualInfo_.FULL_NAME,
            Client_.INDIVIDUAL_INFO + "." + IndividualInfo_.DATE_OF_BIRTH,
            Client_.CONTACT_INFO + "." + ContactInfo_.EMAIL,
            Client_.CONTACT_INFO + "." + ContactInfo_.PHONE
        );
    }
}
// end::class[]
