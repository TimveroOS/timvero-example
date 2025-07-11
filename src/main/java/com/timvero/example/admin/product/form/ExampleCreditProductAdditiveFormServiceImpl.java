package com.timvero.example.admin.product.form;

import com.timvero.application.procuring.ProcuringType;
import com.timvero.example.admin.application.entity.ApplicationType;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import com.timvero.example.admin.product.repository.ExampleCreditProductAdditiveRepository;
import com.timvero.loan.execution_result.ExecutionResultType;
import com.timvero.loan.offer.entity.OfferEngineDescriptor;
import com.timvero.loan.offer.entity.OfferEngineDescriptorRepository;
import com.timvero.loan.product.entity.CreditProductAdditive;
import com.timvero.loan.product.entity.CreditProductAdditiveRepository;
import com.timvero.structure.additional.history.HistoryEntity;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class ExampleCreditProductAdditiveFormServiceImpl implements ExampleCreditProductAdditiveFormService {

    private final ExampleCreditProductAdditiveFormMapper formMapper;
    private final CreditProductAdditiveRepository additiveRepository;
    private final ExampleCreditProductAdditiveRepository exampleCreditProductAdditiveRepository;
    private final OfferEngineDescriptorRepository engineDescriptorRepository;

    public ExampleCreditProductAdditiveFormServiceImpl(ExampleCreditProductAdditiveFormMapper formMapper,
        CreditProductAdditiveRepository additiveRepository,
        ExampleCreditProductAdditiveRepository exampleCreditProductAdditiveRepository,
        OfferEngineDescriptorRepository engineDescriptorRepository) {
        this.formMapper = formMapper;
        this.additiveRepository = additiveRepository;
        this.exampleCreditProductAdditiveRepository = exampleCreditProductAdditiveRepository;
        this.engineDescriptorRepository = engineDescriptorRepository;
    }

    @Override
    @Transactional
    public void saveAdditive(ExampleCreditProductAdditiveForm form) {
        CreditProductAdditive additive = formMapper.createEntity(form);
        additiveRepository.save(additive);
        if (form.getAdditiveId() != null) {
            additiveRepository.removeAdditive(additive.getProduct().getId(), form.getAdditiveId());
        }
    }

    @Override
    @Transactional
    public void assembleEditModel(ExampleCreditProduct product, ExampleCreditProductAdditive additive, Model model) {
        Set<ExecutionResultType> requiredEngineTypes = product.getOfferEngineTypes();

        model.addAttribute("productId", product.getId());
        model.addAttribute("form",
            additive != null ? formMapper.toForm(additive) : new ExampleCreditProductAdditiveForm());
        model.addAttribute("offerEngineTypes", requiredEngineTypes);
        model.addAttribute("offerEngineDescriptors", Lazy.of(() -> getEngineDescriptorMap(requiredEngineTypes)));
        model.addAttribute("procuringTypes", ProcuringType.values());
        model.addAttribute("applicationType", ApplicationType.NORMAL);
    }

    @Override
    public Collection<ExampleCreditProductAdditive> findAllByProduct(ExampleCreditProduct entity) {
        return exampleCreditProductAdditiveRepository.findAllByProduct(entity);
    }

    private Map<ExecutionResultType, Map<UUID, String>> getEngineDescriptorMap(
        Set<ExecutionResultType> executionResultTypes) {
        return engineDescriptorRepository.findByExecutionResultType(executionResultTypes).stream()
            .collect(Collectors.groupingBy(OfferEngineDescriptor::getExecutionResultType, HashMap::new,
                Collectors.toMap(HistoryEntity::getLineageId, OfferEngineDescriptor::getName)));
    }
}
