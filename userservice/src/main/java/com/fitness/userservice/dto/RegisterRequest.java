package com.fitness.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Keycloak ID is required")
    private String keycloakId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    /*
     * Currently required because the existing users table
     * has a NOT NULL password column.
     *
     * Keycloak remains the actual authentication provider.
     */
    private String password;
}