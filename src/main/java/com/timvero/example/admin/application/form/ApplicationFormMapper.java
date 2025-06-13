package com.timvero.example.admin.application.form;

import com.timvero.base.form.EntityToFormMapper;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.participant.form.ParticipantFormMapper;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


// tag::mapper[]
@Mapper(uses = ParticipantFormMapper.class)
public interface ApplicationFormMapper extends EntityToFormMapper<Application, ApplicationForm> {
// end::mapper[]

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Override
    void toEntity(ApplicationForm form, @MappingTarget Application entity);

    @Override
    @InheritInverseConfiguration(name = "toEntity")
    ApplicationForm toForm(Application entity);

    @Override
    @InheritConfiguration
    Application createEntity(ApplicationForm form);

}
