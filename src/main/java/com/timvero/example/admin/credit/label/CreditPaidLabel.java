package com.timvero.example.admin.credit.label;


import static com.timvero.example.admin.credit.CreditCalculationConfiguration.ACTIVE;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.ground.entity_marker.label.EntityLabel;
import java.util.Optional;
import javax.money.MonetaryAmount;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2000)
public class CreditPaidLabel implements EntityLabel<ExampleCredit> {

    @Override
    public boolean isEntityMarked(ExampleCredit entity) {
        if (entity.getActualSnapshot() == null) {
            return false;
        }
        Optional<MonetaryAmount> totalDebt = entity.getActualSnapshot().getDebt().getTotal();
        return entity.getActualSnapshot().getStatus().equals(ACTIVE) && totalDebt.isEmpty();
    }

    @Override
    public String getName() {
        return "paid";
    }
}
