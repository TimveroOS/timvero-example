package com.timvero.example.admin.product.tab;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.form.ExampleCreditProductFormService;
import com.timvero.web.common.tab.EntityTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/additives")
@Controller
@Order(2000)
public class ExampleCreditProductAdditivesTab extends EntityTabController<UUID, ExampleCreditProduct> {

    private final ExampleCreditProductFormService creditProductFormService;

    public ExampleCreditProductAdditivesTab(ExampleCreditProductFormService creditProductFormService) {
        this.creditProductFormService = creditProductFormService;
    }

    @Override
    public boolean isVisible(ExampleCreditProduct entity) {
        return true;
    }

    @Override
    protected String getTabTemplate(UUID id, Model model) throws Exception {
        ExampleCreditProduct creditProduct = loadEntity(id);
        model.addAttribute("additives", creditProductFormService.getAdditives(creditProduct));
        return super.getTabTemplate(id, model);
    }
}
