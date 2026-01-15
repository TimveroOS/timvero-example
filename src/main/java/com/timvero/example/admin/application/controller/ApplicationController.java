package com.timvero.example.admin.application.controller;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.filter.ApplicationFilter;
import com.timvero.web.common.ViewableFilterController;
import com.timvero.web.common.menu.MenuItem;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ApplicationController.PATH)
@MenuItem(order = 5_100, name = "application")
public class ApplicationController extends ViewableFilterController<UUID, Application, ApplicationFilter> {

    public static final String PATH = "/application";

    @Override
    protected String getViewPage() {
        return "/application/view";
    }

    @Override
    protected String getHeaderPage() {
        return "/application/header";
    }
}