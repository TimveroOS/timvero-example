package com.timvero.example.admin.credit.action;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.void_credit.VoidOperationService;
import com.timvero.ground.action.EntityAction;
import com.timvero.web.common.action.SimpleActionController;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/void")
@Controller
@Order(3030)
public class VoidAction extends SimpleActionController<UUID, ExampleCredit> {

    @Autowired
    private VoidOperationService voidOperationService;

    @Override
    protected EntityAction<? super ExampleCredit, Object> action() {
        return when(c -> c.getActualSnapshot() != null && !c.getActualSnapshot().getStatus().isEnding() && c.getActualSnapshot().getDebt().getTotal().isEmpty())
            .then((c, f, u) -> {
                voidOperationService.createAndSaveOperation(c.getId(), LocalDate.now());
            });
    }
}
