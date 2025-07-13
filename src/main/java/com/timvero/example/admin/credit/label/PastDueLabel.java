package com.timvero.example.admin.credit.label;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.pastdue.PastDueOperationService;
import com.timvero.ground.entity_marker.label.EntityLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2100)
public class PastDueLabel implements EntityLabel<ExampleCredit> {

    @Autowired
    private PastDueOperationService pastDueOperationService;

    @Override
    public boolean isEntityMarked(ExampleCredit credit) {
        if (credit.getActualSnapshot() == null || credit.getActualSnapshot().getStatus().isEnding()) {
            return false;
        }
        return pastDueOperationService.isPastDue(credit.getActualSnapshot());
    }

    @Override
    public String getName() {
        return "pastDue";
    }
}
