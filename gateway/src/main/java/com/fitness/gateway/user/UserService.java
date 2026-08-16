package com.fitness.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId) {

        log.info("Validating user {} with userservice", userId);

        return userServiceWebClient
                .get()
                .uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnNext(exists ->
                        log.info(
                                "User {} exists: {}",
                                userId,
                                exists
                        )
                )
                .doOnError(error ->
                        log.error(
                                "Failed to validate user {}",
                                userId,
                                error
                        )
                );
    }

    public Mono<UserResponse> registerUser(RegisterRequest request) {

        log.info(
                "Registering Keycloak user {}",
                request.getKeycloakId()
        );

        return userServiceWebClient
                .post()
                .uri("/api/users/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .doOnSuccess(user ->
                        log.info(
                                "Successfully registered user {}",
                                request.getKeycloakId()
                        )
                )
                .doOnError(error ->
                        log.error(
                                "Failed to register user {}",
                                request.getKeycloakId(),
                                error
                        )
                );
    }
}