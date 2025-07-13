package com.timvero.example.admin.credit.controller;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.credit.filter.ExampleCreditFilter;
import com.timvero.web.common.ViewableFilterController;
import com.timvero.web.common.menu.MenuItem;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = ExampleCreditController.PATH)
@MenuItem(order = 5_300, name = "credit")
public class ExampleCreditController extends ViewableFilterController<UUID, ExampleCredit, ExampleCreditFilter> {

    public static final String PATH = "/credit";

    @Override
    protected String getHeaderPage() {
        return "/credit/header";
    }
}