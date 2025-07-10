package com.timvero.example.admin.credit;

import com.timvero.loan.product.entity.CreditType;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleCreditType {

    public static final CreditType PERSONAL = new CreditType("PERSONAL");
}
