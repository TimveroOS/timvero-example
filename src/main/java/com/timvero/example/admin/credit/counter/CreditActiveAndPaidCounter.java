package com.timvero.example.admin.credit.counter;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.credit.label.CreditPaidLabel;
import com.timvero.ground.entity_marker.counter.EntityCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3000)
public class CreditActiveAndPaidCounter implements EntityCounter<ExampleCredit> {

    @Autowired
    private CreditPaidLabel creditPaidLabel;

    @Override
    public boolean isEntityMarked(ExampleCredit entity) {
        return creditPaidLabel.isEntityMarked(entity);
    }

    @Override
    public String getName() {
        return "creditActiveAndPaid";
    }
}
