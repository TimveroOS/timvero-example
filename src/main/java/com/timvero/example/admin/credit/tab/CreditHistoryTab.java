package com.timvero.example.admin.credit.tab;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.web.common.tab.EntityHistoryTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/history")
@Controller
@Order(10000)
public class CreditHistoryTab extends EntityHistoryTabController<UUID, ExampleCredit> {

}
