package com.timvero.example.admin.product.form;

import com.timvero.base.form.EntityFormService;
import com.timvero.example.admin.offer.ExampleDataProcessor;
import com.timvero.example.admin.participant.document.ApplicationContractDocumentCategory;
import com.timvero.example.admin.product.engine.SimpleScheduledEngine;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.loan.execution_result.ExecutionResultType;
import com.timvero.loan.product.entity.CreditType;
import com.timvero.scheduled.day_count.DayCountMethod;
import com.timvero.structure.template.form.DocumentTemplateFormService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleCreditProductFormService extends EntityFormService<ExampleCreditProduct, CreditProductForm, UUID> {

    @Autowired
    private DocumentTemplateFormService documentTemplateService;
    @Autowired
    protected Map<String, DayCountMethod> dayCountMethods;

    @Override
    protected void assembleEditModel(ExampleCreditProduct entity, CreditProductForm form, Map<String, Object> model) {
        model.put("productEngines", List.of(SimpleScheduledEngine.NAME));
        model.put("offerEngineTypes", new ExecutionResultType[]{ExampleDataProcessor.TYPE});
        model.put("creditProductForm", form);
        model.put("creditTypes", CreditType.values(CreditType.class));
        model.put("templates", documentTemplateService.getTemplatesMap(ApplicationContractDocumentCategory.TYPE));
        model.put("dayCountMethods", dayCountMethods.keySet());
    }

}
