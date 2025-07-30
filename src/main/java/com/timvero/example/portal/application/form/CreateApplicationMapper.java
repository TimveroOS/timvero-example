package com.timvero.example.portal.application.form;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.participant.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreateApplicationMapper {

    @Mapping(target = "borrowerParticipant", source = ".")
    Application toApplication(CreateApplicationRequest request);

    Participant toParticipant(CreateApplicationRequest request);
}
