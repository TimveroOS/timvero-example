package com.timvero.example.admin.product.form;

import com.timvero.base.form.EntityToFormMapper;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExampleCreditProductFormMapper extends EntityToFormMapper<ExampleCreditProduct, CreditProductForm> {

    //ExampleCreditProduct copy(ExampleCreditProduct creditProduct);

    @Override
    void toEntity(CreditProductForm form, @MappingTarget ExampleCreditProduct entity);

    @Override
    @InheritInverseConfiguration(name = "toEntity")
    CreditProductForm toForm(ExampleCreditProduct entity);

    @Override
    @InheritConfiguration
    ExampleCreditProduct createEntity(CreditProductForm form);


}
