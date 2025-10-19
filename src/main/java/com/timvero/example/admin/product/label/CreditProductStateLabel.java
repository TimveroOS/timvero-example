package com.timvero.example.admin.product.label;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.ground.entity_marker.label.EntityStatusLabel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1000)
public class CreditProductStateLabel extends EntityStatusLabel<ExampleCreditProduct> {

    public CreditProductStateLabel() {
        super(ExampleCreditProduct::getState);
    }

    @Override
    public boolean isEntityMarked(ExampleCreditProduct entity) {
        return entity.getState() != null;
    }

    @Override
    public String getGroup() {
        return "state";
    }
}