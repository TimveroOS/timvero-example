package com.timvero.example.admin.product.form;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.loan.product.form.BaseCreditProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ExampleCreditProductFormMapper implements
    BaseCreditProductMapper<ExampleCreditProduct, CreditProductForm> {

    public abstract ExampleCreditProduct copy(ExampleCreditProduct creditProduct);

}
