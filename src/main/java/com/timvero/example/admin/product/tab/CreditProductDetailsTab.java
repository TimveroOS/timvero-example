package com.timvero.example.admin.product.tab;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.web.common.tab.EntityTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/details")
@Controller
@Order(1000)
public class CreditProductDetailsTab extends EntityTabController<UUID, ExampleCreditProduct> {

    @Override
    public boolean isVisible(ExampleCreditProduct entity) {
        return true;
    }
}
