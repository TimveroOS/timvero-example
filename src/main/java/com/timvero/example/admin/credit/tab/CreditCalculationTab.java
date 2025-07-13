package com.timvero.example.admin.credit.tab;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.servicing.credit.CreditViewOptions;
import com.timvero.servicing.credit.tab.AbstractCreditCalculationTab;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/system-calculation")
@Controller
@Order(7100)
@ConditionalOnBean(CreditViewOptions.class)
public class CreditCalculationTab extends AbstractCreditCalculationTab<ExampleCredit> {

}
