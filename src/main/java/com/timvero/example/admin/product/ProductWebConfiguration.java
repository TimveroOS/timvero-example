package com.timvero.example.admin.product;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import com.timvero.example.admin.product.filter.ExampleCreditProductFilter;
import com.timvero.example.admin.product.form.CreditProductForm;
import com.timvero.example.admin.product.form.ExampleCreditProductAdditiveForm;
import com.timvero.loan.product.ProductWebConfigurationSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
public class ProductWebConfiguration extends ProductWebConfigurationSupport {

    public ProductWebConfiguration() {
        super(ExampleCreditProduct.class, CreditProductForm.class, ExampleCreditProductFilter.class,
            ExampleCreditProductAdditive.class, ExampleCreditProductAdditiveForm.class);
    }
}
