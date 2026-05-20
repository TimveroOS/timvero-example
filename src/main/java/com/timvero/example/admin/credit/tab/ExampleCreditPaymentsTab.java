package com.timvero.example.admin.credit.tab;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.servicing.credit.CreditViewOptions;
import com.timvero.servicing.credit.tab.CreditPaymentsTab;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/payments")
@Controller
@Order(8500)
@ConditionalOnBean(CreditViewOptions.class)
public class ExampleCreditPaymentsTab extends CreditPaymentsTab<ExampleCredit> {

}
