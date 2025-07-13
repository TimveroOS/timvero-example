package com.timvero.example.admin.credit.action;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.ground.action.EntityAction;
import com.timvero.servicing.engine.CreditCalculationService;
import com.timvero.web.common.action.SimpleActionController;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/recalculate")
@Controller
@Order(1000)
public class RecalculateAction extends SimpleActionController<UUID, ExampleCredit> {

    @Autowired
    private CreditCalculationService creditCalculationService;

    @Override
    protected EntityAction<? super ExampleCredit, Object> action() {
        return when(c -> c.getActualSnapshot() == null || !c.getActualSnapshot().getStatus().isEnding())
            .then((c, f, u) -> {
                creditCalculationService.calculate(c.getId(), null, LocalDate.now());
            });
    }
}
