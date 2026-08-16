package com.fitness.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String FRONTEND_ORIGIN =
            "http://fitx-frontend-020641930163.s3-website.ap-south-1.amazonaws.com";

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .cors(Customizer.withDefaults())

                .authorizeExchange(exchange -> exchange

                        .pathMatchers("/actuator/health")
                        .permitAll()

                        .anyExchange()
                        .authenticated()
                )

                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(
                                Customizer.withDefaults()
                        )
                )

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config =
                new CorsConfiguration();

        config.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        FRONTEND_ORIGIN
                )
        );

        config.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );

        config.setAllowedHeaders(
                Arrays.asList(
                        "Authorization",
                        "Content-Type",
                        "X-User-ID"
                )
        );

        config.setExposedHeaders(
                List.of("Authorization")
        );

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                config
        );

        return source;
    }
}