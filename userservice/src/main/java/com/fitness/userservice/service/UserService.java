package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UpdateGoalsRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    // =========================================================
    // REGISTER USER
    // =========================================================
    public UserResponse register(@Valid RegisterRequest request) {

        // -----------------------------------------------------
        // Case 1: User already exists with Keycloak ID
        // -----------------------------------------------------
        if (repository.existsByKeycloakId(request.getKeycloakId())) {

            User existingUser =
                    repository.findByKeycloakId(request.getKeycloakId());

            return mapToResponse(existingUser);
        }

        // -----------------------------------------------------
        // Case 2: User already exists with email
        // -----------------------------------------------------
        if (repository.existsByEmail(request.getEmail())) {

            User existingUser =
                    repository.findByEmail(request.getEmail());

            // Attach Keycloak ID if it is missing/different
            if (existingUser.getKeycloakId() == null ||
                    !existingUser.getKeycloakId()
                            .equals(request.getKeycloakId())) {

                existingUser.setKeycloakId(request.getKeycloakId());
            }

            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());

            /*
             * Keycloak is the actual authentication provider.
             * We only populate this field because the current
             * database schema requires password to be non-null.
             */
            if (request.getPassword() != null) {
                existingUser.setPassword(request.getPassword());
            }

            User savedUser = repository.save(existingUser);

            return mapToResponse(savedUser);
        }

        // -----------------------------------------------------
        // Case 3: Completely new user
        // -----------------------------------------------------
        User user = new User();

        user.setEmail(request.getEmail());
        user.setKeycloakId(request.getKeycloakId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        /*
         * IMPORTANT:
         *
         * The database currently has password as NOT NULL.
         * Therefore this must be populated.
         */
        user.setPassword(request.getPassword());

        User savedUser = repository.save(user);

        return mapToResponse(savedUser);
    }

    // =========================================================
    // GET USER PROFILE
    // =========================================================
    public UserResponse getUserProfile(String userId) {
        User user = repository.findByKeycloakId(userId);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        return mapToResponse(user);
    }

    // =========================================================
    // VALIDATE USER BY KEYCLOAK ID
    // =========================================================
    public Boolean existByUserId(String userId) {

        return repository.existsByKeycloakId(userId);
    }

    // =========================================================
    // UPDATE ACTIVE PLAN
    // =========================================================
    public UserResponse updateActivePlan(
            String userId,
            String planId) {

        User user = repository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setActivePlanId(planId);

        User savedUser = repository.save(user);

        return mapToResponse(savedUser);
    }

    // =========================================================
    // CLEAR ACTIVE PLAN
    // =========================================================
    public UserResponse clearActivePlan(String userId) {

        User user = repository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setActivePlanId(null);

        User savedUser = repository.save(user);

        return mapToResponse(savedUser);
    }

    // =========================================================
    // UPDATE GOALS
    // =========================================================
    public UserResponse updateGoals(
            String userId,
            @Valid UpdateGoalsRequest request) {

        User user = repository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (request.getDailyCaloriesGoal() != null) {
            user.setDailyCaloriesGoal(
                    request.getDailyCaloriesGoal()
            );
        }

        if (request.getWeeklyWorkoutMinutesGoal() != null) {
            user.setWeeklyWorkoutMinutesGoal(
                    request.getWeeklyWorkoutMinutesGoal()
            );
        }

        if (request.getTargetWeight() != null) {
            user.setTargetWeight(
                    request.getTargetWeight()
            );
        }

        if (request.getPlanType() != null) {
            user.setPlanType(
                    request.getPlanType()
            );
        }

        User savedUser = repository.save(user);

        return mapToResponse(savedUser);
    }

    // =========================================================
    // MAP ENTITY → RESPONSE
    // =========================================================
    private UserResponse mapToResponse(User user) {

        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());

        userResponse.setCreatedAt(
                user.getCreatedAt()
        );

        userResponse.setUpdatedAt(
                user.getUpdatedAt()
        );

        // Goals
        userResponse.setDailyCaloriesGoal(
                user.getDailyCaloriesGoal()
        );

        userResponse.setWeeklyWorkoutMinutesGoal(
                user.getWeeklyWorkoutMinutesGoal()
        );

        userResponse.setTargetWeight(
                user.getTargetWeight()
        );

        userResponse.setPlanType(
                user.getPlanType()
        );

        // Active plan
        userResponse.setActivePlanId(
                user.getActivePlanId()
        );

        return userResponse;
    }
}