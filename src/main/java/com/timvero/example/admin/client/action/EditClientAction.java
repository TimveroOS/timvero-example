package com.timvero.example.admin.client.action;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.client.form.ClientForm;
import com.timvero.web.common.action.EditEntityActionController;
import java.util.UUID;
import org.springframework.stereotype.Controller;

// tag::action-class[]
@Controller
public class EditClientAction extends EditEntityActionController<UUID, Client, ClientForm> {

}
// end::action-class[]
