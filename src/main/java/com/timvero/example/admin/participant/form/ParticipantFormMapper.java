package com.timvero.example.admin.participant.form;

import com.timvero.base.form.EntityToFormMapper;
import com.timvero.example.admin.participant.entity.Participant;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// tag::mapper[]
@Mapper
public interface ParticipantFormMapper extends EntityToFormMapper<Participant, ParticipantForm> {
// end::mapper[]

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Override
    void toEntity(ParticipantForm form, @MappingTarget Participant entity);

    @Override
    @InheritInverseConfiguration(name = "toEntity")
    ParticipantForm toForm(Participant entity);

    @Override
    @InheritConfiguration
    Participant createEntity(ParticipantForm form);
}
