package com.fitness.aiservice.dto;

import lombok.Data;

@Data
public class GoalsRequest {
    private String userId;
    private String goalType;          // PlanType string (WEIGHT_LOSS, MUSCLE_GAIN etc.)
    private Integer targetCalories;
    private String experienceLevel;   // BEGINNER, INTERMEDIATE, ADVANCED
}
