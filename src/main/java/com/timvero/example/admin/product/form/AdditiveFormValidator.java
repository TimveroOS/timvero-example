package com.timvero.example.admin.product.form;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.repository.ExampleCreditProductRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AdditiveFormValidator implements Validator {

    private final ExampleCreditProductRepository creditProductRepository;

    public AdditiveFormValidator(ExampleCreditProductRepository creditProductRepository) {
        this.creditProductRepository = creditProductRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ExampleCreditProductAdditiveForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ExampleCreditProductAdditiveForm form = (ExampleCreditProductAdditiveForm) target;

        ExampleCreditProduct product = creditProductRepository.getReferenceById(form.getProduct());

        BigDecimal minAmount = form.getMinAmount();
        BigDecimal maxAmount = form.getMaxAmount();

        validateAmount(minAmount, product, "minAmount", errors);
        validateAmount(maxAmount, product, "maxAmount", errors);

        if (minAmount.compareTo(maxAmount) > 0) {
            errors.rejectValue("minAmount", "error.product.additive.minMaxAmount");
            errors.rejectValue("maxAmount", "error.product.additive.minMaxAmount");
        }

        Integer minTerm = form.getMinTerm();
        Integer maxTerm = form.getMaxTerm();

        validateTerm(minTerm, product, "minTerm", errors);
        validateTerm(maxTerm, product, "maxTerm", errors);

        if (minTerm > maxTerm) {
            errors.rejectValue("minTerm", "error.product.additive.minMaxTerm");
            errors.rejectValue("maxTerm", "error.product.additive.minMaxTerm");
        }
    }

    private void validateAmount(BigDecimal amount, ExampleCreditProduct product, String field, Errors errors) {
        if (amount.compareTo(product.getMinAmount()) < 0) {
            errors.rejectValue(field, "error.product.additive.minAmount");
        } else if (amount.compareTo(product.getMaxAmount()) > 0) {
            errors.rejectValue(field, "error.product.additive.maxAmount");
        }
    }

    private void validateTerm(Integer term, ExampleCreditProduct product, String field, Errors errors) {
        if (term < product.getMinTerm()) {
            errors.rejectValue(field, "error.product.additive.minTerm");
        } else if (term > product.getMaxTerm()) {
            errors.rejectValue(field, "error.product.additive.maxTerm");
        }
    }
}
