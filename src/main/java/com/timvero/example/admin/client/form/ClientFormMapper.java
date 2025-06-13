package com.timvero.example.admin.client.form;

import com.timvero.base.form.EntityToFormMapper;
import com.timvero.example.admin.client.entity.Client;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// tag::mapper[]
@Mapper
public interface ClientFormMapper extends EntityToFormMapper<Client, ClientForm> {
// end::mapper[]

    @Mapping(target = "allNotificationEmails", ignore = true)
    @Mapping(target = "allNotificationPhones", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Override
    void toEntity(ClientForm form, @MappingTarget Client entity);

    @Override
    @InheritInverseConfiguration(name = "toEntity")
    ClientForm toForm(Client entity);

    @Override
    @InheritConfiguration
    Client createEntity(ClientForm form);
}
