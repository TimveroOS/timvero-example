package com.timvero.example;

import com.timvero.BaseConfiguration;
import com.timvero.example.admin.CustomConfiguration;
import com.timvero.example.admin.CustomWebConfiguration;
import com.timvero.example.portal.PortalWebConfiguration;
import com.timvero.flowable.external.ExternalProcessWebMvcConfig;
import com.timvero.web.WebMvcConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder parentBuilder =
            new SpringApplicationBuilder(BaseConfiguration.class, CustomConfiguration.class)
                .web(WebApplicationType.NONE);
        parentBuilder.run(args);
        parentBuilder.child(WebMvcConfig.class, CustomWebConfiguration.class)
            .properties("server.port=8081")
            .run(args);
        parentBuilder.child(PortalWebConfiguration.class)
            .properties("server.port=8082")
            .run(args);

        parentBuilder.child(ExternalProcessWebMvcConfig.class)
            .properties("spring.config.name=workflow")
            .run(args);
    }

}
