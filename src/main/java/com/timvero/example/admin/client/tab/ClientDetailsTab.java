package com.timvero.example.admin.client.tab;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.web.common.tab.EntityTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/details")
@Controller
@Order(1000)
public class ClientDetailsTab extends EntityTabController<UUID, Client> {

    @Override
    public boolean isVisible(Client client) {
        return true;
    }
}
