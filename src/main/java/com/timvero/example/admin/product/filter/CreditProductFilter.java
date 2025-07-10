package com.timvero.example.admin.product.filter;

import com.timvero.ground.filter.annotation.Field;
import com.timvero.ground.filter.annotation.Restriction;
import com.timvero.ground.filter.base.ListFilter;

public class CreditProductFilter extends ListFilter {

    @Field(restriction = Restriction.EQ, value = "active")
    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
