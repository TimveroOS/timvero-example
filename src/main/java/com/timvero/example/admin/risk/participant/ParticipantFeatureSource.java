package com.timvero.example.admin.risk.participant;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.ground.util.EntityUtils;
import com.timvero.loan.feature.service.EntityFeatureSource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component(ParticipantFeatureSource.APPLICATION_DATA_SOURCE_NAME)
public class ParticipantFeatureSource implements EntityFeatureSource<Participant> {

    public static final String APPLICATION_DATA_SOURCE_NAME = "applicationDataSource";

    @Override
    public Map<String, ?> getBindings(Participant participant) {
        Map<String, Object> bindings = new HashMap<>();

        bindings.put("participant", EntityUtils.initializeAndUnproxy(participant));
        bindings.put("application", EntityUtils.initializeAndUnproxy(participant.getApplication()));

        Client client = EntityUtils.initializeAndUnproxy(participant.getClient());
        if (client != null) {
            bindings.put("client", client);
        }
        return bindings;
    }
}
