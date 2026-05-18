package com.timvero.example.admin.client.tab;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.web.common.tab.EntityTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/applications")
@Controller
@Order(5000)
public class ClientApplicationsTab extends EntityTabController<UUID, Client> {

    @Override
    public boolean isVisible(Client client) {
        return !client.getApplications().isEmpty();
    }

    @Override
    protected String getTabTemplate(UUID clientId, Model model) throws Exception {
        Client client = loadEntity(clientId);
        model.addAttribute("clientApplications", client.getApplications());
        return super.getTabTemplate(clientId, model);
    }
}
