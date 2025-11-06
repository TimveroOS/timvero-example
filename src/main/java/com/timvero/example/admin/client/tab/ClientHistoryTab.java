package com.timvero.example.admin.client.tab;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.web.common.tab.EntityHistoryTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;

@Controller
@Order(10000)
public class ClientHistoryTab extends EntityHistoryTabController<UUID, Client> {
}