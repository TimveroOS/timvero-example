package com.timvero.example.admin.product;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.filter.CreditProductFilter;
import com.timvero.web.common.ViewableFilterController;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class CreditProductController extends ViewableFilterController<UUID, ExampleCreditProduct, CreditProductFilter> {

}
