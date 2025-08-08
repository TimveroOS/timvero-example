package com.timvero.example.admin.client;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.ground.service.fastsearch.FastSearchEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("hibernate.search.enabled")
public class ClientSearchConfig {

    @Bean
    FastSearchEntity<Client> clientSearchEntity() {
        return new FastSearchEntity<>(Client.class, Client::getDisplayedName);
    }

}