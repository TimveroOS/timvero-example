package com.timvero.example.admin.client.action;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.client.form.ClientForm;
import com.timvero.web.common.action.EntityCreateController;
import java.util.UUID;
import org.springframework.stereotype.Controller;

// tag::action-class[]
@Controller
public class CreateClientAction extends EntityCreateController<UUID, Client, ClientForm> {

    @Override
    protected boolean isOwnPage() {
        return false;
    }
}
// end::action-class[]
