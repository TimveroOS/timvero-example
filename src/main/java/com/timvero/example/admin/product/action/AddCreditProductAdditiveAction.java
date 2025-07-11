package com.timvero.example.admin.product.action;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.form.ExampleCreditProductAdditiveForm;
import com.timvero.example.admin.product.form.ExampleCreditProductAdditiveFormService;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.entity.reload.ReloadPageHelper;
import com.timvero.web.common.action.EntityActionController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/add-product-additive")
@Order(10_000)
public class AddCreditProductAdditiveAction extends
    EntityActionController<UUID, ExampleCreditProduct, ExampleCreditProductAdditiveForm> {

    private final ExampleCreditProductAdditiveFormService additiveFormService;
    private final ReloadPageHelper reloadPageHelper;

    public AddCreditProductAdditiveAction(ExampleCreditProductAdditiveFormService additiveFormService,
        ReloadPageHelper reloadPageHelper) {
        this.additiveFormService = additiveFormService;
        this.reloadPageHelper = reloadPageHelper;
    }

    @Override
    protected EntityAction<ExampleCreditProduct, ExampleCreditProductAdditiveForm> action() {
        return when(ExampleCreditProduct::isActive)
            .then((p, f, u) -> {
                additiveFormService.saveAdditive(f);
                reloadPageHelper.reload(p); // no auto reload because of direct DB change
            });
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) {
        ExampleCreditProduct creditProduct = loadEntity(id);
        additiveFormService.assembleEditModel(creditProduct, null, model);
        model.addAttribute("engineName", creditProduct.getEngineName());
        return "/product/action/add-product-additive";
    }

    @Override
    public String getHighlighted() {
        return BTN_LIGHT;
    }
}
