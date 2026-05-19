package com.timvero.example;

import com.timvero.BaseConfiguration;
import com.timvero.example.admin.CustomConfiguration;
import com.timvero.example.admin.CustomWebConfiguration;
import com.timvero.example.portal.PortalWebConfiguration;
import com.timvero.flowable.external.ExternalProcessWebMvcConfig;
import com.timvero.web.WebMvcConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

// tag::entry-point[]
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder parentBuilder =
            new SpringApplicationBuilder(BaseConfiguration.class, CustomConfiguration.class)
                .web(WebApplicationType.NONE);
        ConfigurableApplicationContext parentContext = parentBuilder.run(args);
        try {
            parentBuilder.child(ExternalProcessWebMvcConfig.class).web(WebApplicationType.SERVLET)
                .properties("server.port=${process.engine.callbackPort:8380}",
                    "server.servlet.context-path=${workflow.server.context-path:/external-process}",
                    "management.server.port=-1")
                .run(args);

            parentBuilder.child(WebMvcConfig.class, CustomWebConfiguration.class).web(WebApplicationType.SERVLET)
                .properties("server.port=${admin.server.port:8080}",
                    "server.servlet.context-path=${admin.server.context-path:/}",
                    "management.server.port=8189")
                .run(args);

            parentBuilder.child(PortalWebConfiguration.class).web(WebApplicationType.SERVLET)
                .properties("server.port=${portal.server.port:8580}",
                    "server.servlet.context-path=${portal.server.context-path:/}",
                    "management.server.port=-1")
                .run(args);
        } catch (Throwable e) {
            parentContext.close();
            throw e;
        }
    }
}
// end::entry-point[]