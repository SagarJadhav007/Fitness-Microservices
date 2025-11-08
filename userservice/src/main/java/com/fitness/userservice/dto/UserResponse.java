package com.fitness.userservice.dto;

import com.fitness.userservice.model.PlanType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String id;
    private String keycloakId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer dailyCaloriesGoal;
    private Integer weeklyWorkoutMinutesGoal;
    private Double targetWeight;
    private PlanType planType;

    // Active workout plan reference
    private String activePlanId;
}
