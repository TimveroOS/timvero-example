package com.timvero.example.admin.credit.action;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.loan.covenantexecution.action.AbstractCalculateCovenantAction;
import com.timvero.structure.user.entity.UserAccount;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/calculate-covenant")
@Controller
@Order(11000)
public class CalculateCovenantAction extends AbstractCalculateCovenantAction<ExampleCredit> {

    @Override
    protected boolean isAvailable(ExampleCredit entity, UserAccount user) {
        return !entity.getActualSnapshot().getStatus().isEnding();
    }
}
