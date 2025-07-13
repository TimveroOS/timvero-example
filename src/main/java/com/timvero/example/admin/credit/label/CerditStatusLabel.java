package com.timvero.example.admin.credit.label;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.ground.entity_marker.label.EntityStatusLabel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1000)
public class CerditStatusLabel extends EntityStatusLabel<ExampleCredit> {

    public CerditStatusLabel() {
        super(credit -> credit.getActualSnapshot() != null ? credit.getActualSnapshot().getStatus() : null);
    }

}
