package com.timvero.example.admin.client.controller;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.client.filter.ClientFilter;
import com.timvero.web.common.ViewableFilterController;
import com.timvero.web.common.menu.MenuItem;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("client")
@MenuItem(order = 5_500, name = "client")
public class ClientController extends ViewableFilterController<UUID, Client, ClientFilter>{

    @Override
    protected String getHeaderPage() {
        return "/client/header";
    }
}
