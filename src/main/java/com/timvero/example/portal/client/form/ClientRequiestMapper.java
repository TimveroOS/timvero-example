package com.timvero.example.portal.client.form;

import com.timvero.example.admin.client.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClientRequiestMapper {

    @Mapping(target = "individualInfo.nationalId", source = "nationalId")
    @Mapping(target = "individualInfo.fullName", source = "fullName")
    @Mapping(target = "individualInfo.dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "individualInfo.residenceCountry", source = "residenceCountry")
    @Mapping(target = "contactInfo.email", source = "email")
    @Mapping(target = "contactInfo.phone", source = "phone")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "allNotificationEmails", ignore = true)
    @Mapping(target = "allNotificationPhones", ignore = true)
    Client createEntity(CreateClientRequest form);
}
