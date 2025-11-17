package com.timvero.example.admin.credit.action;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.loan.metric.action.AbstractUpdateAnchoredMetricAction;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/update-anchored-metric")
@Controller
@Order(10000)
public class UpdateAnchoredMetricAction extends AbstractUpdateAnchoredMetricAction<ExampleCredit> {

}

