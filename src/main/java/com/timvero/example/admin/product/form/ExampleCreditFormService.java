package com.timvero.example.admin.product.form;

import com.timvero.base.form.EntityFormService;
import com.timvero.example.admin.application.entity.ApplicationType;
import com.timvero.example.admin.offer.ExampleOfferEngineData;
import com.timvero.example.admin.product.engine.SimpleScheduledEngine;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.loan.execution_result.ExecutionResultType;
import com.timvero.loan.product.entity.CreditType;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ExampleCreditFormService extends EntityFormService<ExampleCreditProduct, CreditProductForm, UUID> {

    @Override
    protected void assembleEditModel(ExampleCreditProduct entity, CreditProductForm form, Map<String, Object> model) {
        model.put("productEngines", SimpleScheduledEngine.NAME);
        model.put("offerEngineTypes", new ExecutionResultType[]{ExampleOfferEngineData.TYPE});
        model.put("applicationTypes", ApplicationType.values());
        model.put("creditProductForm", form);
        model.put("creditTypes", CreditType.values(CreditType.class));
    }

}
