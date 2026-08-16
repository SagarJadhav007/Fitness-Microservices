package com.fitness.gateway;

import com.fitness.gateway.user.RegisterRequest;
import com.fitness.gateway.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain) {

        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(authentication -> {

                    if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
                        log.warn("Authenticated principal is not a JWT");
                        return chain.filter(exchange);
                    }

                    String keycloakId = jwt.getSubject();

                    if (keycloakId == null || keycloakId.isBlank()) {
                        log.warn("JWT does not contain subject");
                        return chain.filter(exchange);
                    }

                    log.info("Processing Keycloak user: {}", keycloakId);

                    return userService.validateUser(keycloakId)
                            .flatMap(exists -> {

                                if (Boolean.TRUE.equals(exists)) {
                                    log.info(
                                            "User {} already exists. Skipping registration.",
                                            keycloakId
                                    );

                                    return continueRequest(
                                            exchange,
                                            chain,
                                            keycloakId
                                    );
                                }

                                log.info(
                                        "User {} does not exist. Registering...",
                                        keycloakId
                                );

                                RegisterRequest request =
                                        buildRegisterRequest(jwt);

                                return userService
                                        .registerUser(request)
                                        .doOnSuccess(user ->
                                                log.info(
                                                        "User {} registered successfully.",
                                                        keycloakId
                                                )
                                        )
                                        .then(
                                                continueRequest(
                                                        exchange,
                                                        chain,
                                                        keycloakId
                                                )
                                        );
                            });
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<Void> continueRequest(
            ServerWebExchange exchange,
            WebFilterChain chain,
            String userId) {

        ServerHttpRequest mutatedRequest =
                exchange.getRequest()
                        .mutate()
                        .header("X-User-ID", userId)
                        .build();

        return chain.filter(
                exchange.mutate()
                        .request(mutatedRequest)
                        .build()
        );
    }

    private RegisterRequest buildRegisterRequest(Jwt jwt) {

        RegisterRequest request = new RegisterRequest();

        request.setKeycloakId(jwt.getSubject());
        request.setEmail(jwt.getClaimAsString("email"));
        request.setFirstName(jwt.getClaimAsString("given_name"));
        request.setLastName(jwt.getClaimAsString("family_name"));

        // Not actually used by userservice.register()
        request.setPassword("dummy@123123");

        return request;
    }
}