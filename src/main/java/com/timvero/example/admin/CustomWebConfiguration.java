package com.timvero.example.admin;

import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import com.timvero.ground.document.signable.SignableDocument;
import com.timvero.web.common.action.EntityActionPath;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(includeFilters = {@Filter({Controller.class, ControllerAdvice.class, Configuration.class})}, useDefaultFilters = false)
@ConditionalOnWebApplication
public class CustomWebConfiguration {

    @Bean
    EntityActionPath customEntityPath() {
        return EntityActionPath.builder()
            .addPath(SignableDocument.class, "/signable-document")
            .addPath(ExampleProductOffer.class, "/product-offer")
            .addPath(ExampleCreditProductAdditive.class, "/product-additive")
            .build();
    }
}
