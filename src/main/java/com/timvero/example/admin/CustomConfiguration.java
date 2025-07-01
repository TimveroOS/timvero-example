package com.timvero.example.admin;

import com.timvero.base.TimveroEnableJpa;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.flowable.internal.mapping.entity.DecisionProcessType;
import com.timvero.ground.security.user.UserRoleConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = @Filter({Controller.class, ControllerAdvice.class}))
@TimveroEnableJpa
public class CustomConfiguration {

    public static final DecisionProcessType<Participant> PARTICIPANT_TREE =
        new DecisionProcessType<>("PARTICIPANT_TREE", Participant.class);

    @Bean
    UserRoleConfiguration roleConfiguration() {
        return new UserRoleConfiguration("role.configuration.custom")
            .setPermissionContainers(CustomPermission.class);
    }
}
