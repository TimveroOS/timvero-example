package com.timvero.example.admin.credit.action;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.loan.metric.action.AbstractUpdateAnchoredMetricAction;
import com.timvero.structure.user.entity.UserAccount;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/update-anchored-metric")
@Controller
@Order(10000)
public class UpdateAnchoredMetricAction extends AbstractUpdateAnchoredMetricAction<ExampleCredit> {

    @Override
    protected boolean isAvailable(ExampleCredit entity, UserAccount user) {
        return !entity.getActualSnapshot().getStatus().isEnding();
    }
}

