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
		// Parent context — not a web application.
		// BaseConfiguration is provided by the core.
		// CustomConfiguration is your project-specific configuration.
		SpringApplicationBuilder parentBuilder =
			new SpringApplicationBuilder(BaseConfiguration.class, CustomConfiguration.class)
					.web(WebApplicationType.NONE);

		ConfigurableApplicationContext parentContext = parentBuilder.run(args);
		try {
			// Main admin interface
			parentBuilder.child(WebMvcConfig.class, CustomWebConfiguration.class)
				.properties("spring.config.name=main")
				.run(args);

			// REST API (optional)
			parentBuilder.child(PortalWebConfiguration.class)
				.properties("spring.config.name=portal")
				.run(args);

			// Workflow engine (optional)
			parentBuilder.child(ExternalProcessWebMvcConfig.class)
				.properties("spring.config.name=workflow")
				.run(args);
		} catch (Throwable e) {
			parentContext.close();
			throw e;
		}
	}

}
// end::entry-point[]
