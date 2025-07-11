package com.timvero.example.admin.product;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.filter.CreditProductFilter;
import com.timvero.web.common.ViewableFilterController;
import com.timvero.web.common.menu.MenuItem;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
@MenuItem(order = 900_110, name = "product", group = "setting")
public class CreditProductController extends ViewableFilterController<UUID, ExampleCreditProduct, CreditProductFilter> {

}
