package com.timvero.example.portal;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, proxyTargetClass = true)
@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = InternalApiSecurityConfig.BASIC_AUTH,
    scheme = "basic")
public class InternalApiSecurityConfig {

    public static final String BASIC_AUTH = "basicAuth";
    private static final String[] SWAGGER_PATTERNS = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"};

    @Value("${internal.api.username:username}")
    private String internalApiUser;

    @Value("${internal.api.password:password}")
    private String internalApiPassword;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(final PasswordEncoder passwordEncoder) {
        UserDetails userDetails = User.builder()
            .username(internalApiUser)
            .password(internalApiPassword)
            .passwordEncoder(passwordEncoder::encode)
            .roles()
            .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    protected SecurityFilterChain flowableFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(requests ->
            requests.requestMatchers(SWAGGER_PATTERNS).permitAll());
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());

        return http.build();
    }
}
