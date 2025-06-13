package com.timvero.example.admin;

import com.timvero.base.TimveroEnableJpa;
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

    @Bean
    UserRoleConfiguration roleConfiguration() {
        return new UserRoleConfiguration("role.configuration.custom")
            .setPermissionContainers(CustomPermission.class);
    }
}
