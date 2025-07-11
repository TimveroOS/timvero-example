package com.timvero.example.admin.product.form;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import java.util.Collection;
import org.springframework.ui.Model;

public interface ExampleCreditProductAdditiveFormService {

    void saveAdditive(ExampleCreditProductAdditiveForm form);

    void assembleEditModel(ExampleCreditProduct product, ExampleCreditProductAdditive additive, Model model);

    Collection<ExampleCreditProductAdditive> findAllByProduct(ExampleCreditProduct entity);
}
