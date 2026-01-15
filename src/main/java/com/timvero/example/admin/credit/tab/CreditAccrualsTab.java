package com.timvero.example.admin.credit.tab;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.servicing.credit.tab.AbstractCreditAccrualsTab;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/credit-accruals")
@Controller
@Order(10002)
public class CreditAccrualsTab extends AbstractCreditAccrualsTab<ExampleCredit> {

}
