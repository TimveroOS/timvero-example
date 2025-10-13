package com.timvero.example.admin.product.form;

import com.timvero.application.procuring.ProcuringType;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import com.timvero.loan.execution_result.ExecutionResultType;
import com.timvero.loan.offer.entity.OfferEngineDescriptor;
import com.timvero.loan.offer.entity.OfferEngineDescriptorRepository;
import com.timvero.loan.product.entity.CreditProduct;
import com.timvero.loan.product.form.CreditProductAdditiveFormService;
import com.timvero.structure.additional.history.HistoryEntity;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ExampleCreditProductAdditiveFormServiceImpl extends
    CreditProductAdditiveFormService<ExampleCreditProductAdditive, ExampleCreditProductAdditiveForm>{

     private final OfferEngineDescriptorRepository offerEngineDescriptorRepository;

    public ExampleCreditProductAdditiveFormServiceImpl(
        OfferEngineDescriptorRepository offerEngineDescriptorRepository) {
        this.offerEngineDescriptorRepository = offerEngineDescriptorRepository;
    }

    @Override
    public Collection<?> findAdditiveModelsByProduct(CreditProduct product) {
        return product.getAdditives();
    }

    @Override
    protected void assembleEditModel(@Nullable ExampleCreditProductAdditive entity,
        ExampleCreditProductAdditiveForm form, Map<String, Object> model) {
        model.put("procuringTypes", ProcuringType.values());
    }

    @Override
    public void assembleProductData(Model model, CreditProduct product) {
        ExampleCreditProduct exampleCreditProduct = (ExampleCreditProduct) product;
        Set<ExecutionResultType> requiredEngineTypes = exampleCreditProduct.getOfferEngineTypes();

        model.addAttribute("productId", product.getId());
        model.addAttribute("offerEngineTypes", requiredEngineTypes);
        model.addAttribute("offerEngineDescriptors", Lazy.of(() -> getEngineDescriptorMap(requiredEngineTypes)));
    }

    private Map<ExecutionResultType, Map<UUID, String>> getEngineDescriptorMap(
        Set<ExecutionResultType> executionResultTypes) {
        return offerEngineDescriptorRepository.findByExecutionResultType(executionResultTypes).stream()
            .collect(Collectors.groupingBy(OfferEngineDescriptor::getExecutionResultType, HashMap::new,
                Collectors.toMap(HistoryEntity::getLineageId, OfferEngineDescriptor::getName)));
    }
}
