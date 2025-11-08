package com.fitness.userservice.dto;

import com.fitness.userservice.model.PlanType;
import lombok.Data;

@Data
public class UpdateGoalsRequest {
    private Integer dailyCaloriesGoal;
    private Integer weeklyWorkoutMinutesGoal;
    private Double targetWeight;
    private PlanType planType;
}

