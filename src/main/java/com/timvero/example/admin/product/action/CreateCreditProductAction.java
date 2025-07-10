package com.timvero.example.admin.product.action;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.form.CreditProductForm;
import com.timvero.web.common.action.EntityCreateController;
import java.util.UUID;
import org.springframework.stereotype.Controller;

@Controller
public class CreateCreditProductAction extends EntityCreateController<UUID, ExampleCreditProduct, CreditProductForm> {
    @Override
    protected boolean isOwnPage() {
        return true;
    }
}
