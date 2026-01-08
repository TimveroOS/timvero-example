package com.timvero.example.admin.product.label;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.ground.entity_marker.label.EntityLabel;
import org.springframework.stereotype.Component;

@Component
public class CreditProductEmptyAdditivesLabel implements EntityLabel<ExampleCreditProduct> {

    @Override
    public boolean isEntityMarked(ExampleCreditProduct entity) {
        return entity.getAdditives().isEmpty();
    }

    @Override
    public String getName() {
        return "creditProductEmptyAdditives";
    }
}
