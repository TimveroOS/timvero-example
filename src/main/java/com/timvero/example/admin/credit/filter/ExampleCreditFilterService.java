package com.timvero.example.admin.credit.filter;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.ground.entity.enums.CustomEnum;
import com.timvero.ground.filter.list.FilterListService;
import com.timvero.ground.filter.list.FilterValues;
import com.timvero.servicing.credit.entity.CreditStatus;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class ExampleCreditFilterService extends FilterListService<ExampleCredit, ExampleCreditFilter> {

    @Override
    protected FilterValues getFilterValues(Locale locale) {
        FilterValues filterValues = super.getFilterValues(locale);
        filterValues.add("status", CustomEnum.values(CreditStatus.class), locale);
        return filterValues;
    }

}