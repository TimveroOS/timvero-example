package com.timvero.example.admin.credit.filter;

import com.timvero.example.admin.credit.entity.ExampleCredit_;
import com.timvero.ground.filter.annotation.Field;
import com.timvero.ground.filter.annotation.Restriction;
import com.timvero.ground.filter.base.ListFilter;
import com.timvero.servicing.credit.entity.CreditSnapshot_;
import com.timvero.servicing.credit.entity.CreditStatus;

public class ExampleCreditFilter extends ListFilter {

    @Field(restriction = Restriction.IN, value = ExampleCredit_.ACTUAL_SNAPSHOT + "." + CreditSnapshot_.STATUS)
    private CreditStatus[] status;

    public CreditStatus[] getStatus() {
        return status;
    }

    public void setStatus(CreditStatus[] status) {
        this.status = status;
    }
}