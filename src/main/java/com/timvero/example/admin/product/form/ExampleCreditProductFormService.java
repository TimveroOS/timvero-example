package com.timvero.example.admin.product.form;

import com.timvero.base.form.EntityFormService;
import com.timvero.example.admin.offer.ExampleDataProcessor;
import com.timvero.example.admin.participant.document.ApplicationContractDocumentCategory;
import com.timvero.example.admin.product.engine.SimpleScheduledEngine;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import com.timvero.example.admin.product.repository.ExampleCreditProductRepository;
import com.timvero.loan.execution_result.ExecutionResultType;
import com.timvero.loan.product.entity.CreditProductAdditive;
import com.timvero.loan.product.entity.CreditType;
import com.timvero.structure.template.form.DocumentTemplateFormService;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ExampleCreditProductFormService extends EntityFormService<ExampleCreditProduct, CreditProductForm, UUID> {

    private final DocumentTemplateFormService documentTemplateService;
    private final ExampleCreditProductAdditiveFormService exampleCreditProductAdditiveFormService;
    private final ExampleCreditProductRepository creditProductRepository;

    public ExampleCreditProductFormService(DocumentTemplateFormService documentTemplateService,
        ExampleCreditProductAdditiveFormService exampleCreditProductAdditiveFormService,
        ExampleCreditProductRepository creditProductRepository) {
        this.documentTemplateService = documentTemplateService;
        this.exampleCreditProductAdditiveFormService = exampleCreditProductAdditiveFormService;
        this.creditProductRepository = creditProductRepository;
    }

    @Override
    protected void assembleEditModel(ExampleCreditProduct entity, CreditProductForm form, Map<String, Object> model) {
        model.put("productEngines", SimpleScheduledEngine.NAME);
        model.put("offerEngineTypes", new ExecutionResultType[]{ExampleDataProcessor.TYPE});
        model.put("creditProductForm", form);
        model.put("creditTypes", CreditType.values(CreditType.class));
        model.put("templates", documentTemplateService.getTemplatesMap(ApplicationContractDocumentCategory.TYPE));
    }

    public List<ExampleCreditProductAdditive> getAdditives(ExampleCreditProduct entity) {
        return exampleCreditProductAdditiveFormService.findAllByProduct(entity).stream()
            .sorted(Comparator.comparing(CreditProductAdditive::isActive).reversed())
            .sorted(Comparator.comparing(CreditProductAdditive::getUpdatedAt).reversed())
            .collect(Collectors.toCollection(LinkedList::new));
    }

}
