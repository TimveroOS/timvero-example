package com.timvero.example.admin.credit.tab;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.loan.metric.tab.HasCovenantAnchoredMetricTab;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/anchored-metric")
@Controller
@Order(10000)
public class CreditAnchoredMetricTab extends HasCovenantAnchoredMetricTab<ExampleCredit> {

    @Override
    public boolean isVisible(ExampleCredit entity) {
        return true;
    }
}
