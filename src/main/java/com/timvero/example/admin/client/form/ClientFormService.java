package com.timvero.example.admin.client.form;

import com.timvero.base.form.EntityFormService;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.client.entity.Country;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

// tag::form-service[]
@Service
public class ClientFormService extends EntityFormService<Client, ClientForm, UUID> {
// end::form-service[]

    @Override
    protected void assembleEditModel(Client entity, ClientForm form, Map<String, Object> model) {
        model.put("countries", Country.values());
    }

}
