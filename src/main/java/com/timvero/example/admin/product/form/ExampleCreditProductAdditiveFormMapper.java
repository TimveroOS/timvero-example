package com.timvero.example.admin.product.form;

import com.timvero.base.form.EntityToFormMapper;
import com.timvero.base.mapping.ReferenceMapper;
import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(uses = ReferenceMapper.class)
public interface ExampleCreditProductAdditiveFormMapper extends
    EntityToFormMapper<ExampleCreditProductAdditive, ExampleCreditProductAdditiveForm> {

    @Override
    void toEntity(ExampleCreditProductAdditiveForm form, @MappingTarget ExampleCreditProductAdditive entity);

    @Override
    @InheritInverseConfiguration(name = "toEntity")
    ExampleCreditProductAdditiveForm toForm(ExampleCreditProductAdditive entity);

    @Override
    @InheritConfiguration
    ExampleCreditProductAdditive createEntity(ExampleCreditProductAdditiveForm form);

    @AfterMapping
    default void afterToForm(@MappingTarget ExampleCreditProductAdditiveForm form,
        ExampleCreditProductAdditive additive) {
        form.setAdditiveId(additive.getId());
    }
}
