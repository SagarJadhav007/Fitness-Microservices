package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UpdateGoalsRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fitness.userservice.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public UserResponse register(@Valid RegisterRequest request) {

        // Case 1: User already exists with Keycloak ID
        if (repository.existsByKeycloakId(request.getKeycloakId())) {
            User existingUser = repository.findByKeycloakId(request.getKeycloakId());
            return mapToResponse(existingUser);
        }

        // Case 2: User exists by email
        if (repository.existsByEmail(request.getEmail())) {
            User existingUser = repository.findByEmail(request.getEmail());

            if (existingUser.getKeycloakId() == null ||
                    !existingUser.getKeycloakId().equals(request.getKeycloakId())) {
                existingUser.setKeycloakId(request.getKeycloakId());
            }

            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());

            existingUser = repository.save(existingUser);
            return mapToResponse(existingUser);
        }

        // Case 3: New user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setKeycloakId(request.getKeycloakId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = repository.save(user);
        return mapToResponse(savedUser);
    }

    public UserResponse getUserProfile(String userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return mapToResponse(user);
    }

    public Boolean existByUserId(String userId) {
        return repository.existsByKeycloakId(userId);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        // Goals
        userResponse.setDailyCaloriesGoal(user.getDailyCaloriesGoal());
        userResponse.setWeeklyWorkoutMinutesGoal(user.getWeeklyWorkoutMinutesGoal());
        userResponse.setTargetWeight(user.getTargetWeight());
        userResponse.setPlanType(user.getPlanType());

        // Active Plan
        userResponse.setActivePlanId(user.getActivePlanId());

        return userResponse;
    }

    public UserResponse updateActivePlan(String userId, String planId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActivePlanId(planId);

        User saved = repository.save(user);
        return mapToResponse(saved);
    }


    public UserResponse clearActivePlan(String userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActivePlanId(null);

        User saved = repository.save(user);
        return mapToResponse(saved);
    }


    public UserResponse updateGoals(String userId, @Valid UpdateGoalsRequest request) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getDailyCaloriesGoal() != null) {
            user.setDailyCaloriesGoal(request.getDailyCaloriesGoal());
        }

        if (request.getWeeklyWorkoutMinutesGoal() != null) {
            user.setWeeklyWorkoutMinutesGoal(request.getWeeklyWorkoutMinutesGoal());
        }

        if (request.getTargetWeight() != null) {
            user.setTargetWeight(request.getTargetWeight());
        }

        if (request.getPlanType() != null) {
            user.setPlanType(request.getPlanType());
        }

        User saved = repository.save(user);
        return mapToResponse(saved);
    }

}
