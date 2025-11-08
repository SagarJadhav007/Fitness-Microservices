package com.fitness.userservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkoutPlanResponse {

    private String id;
    private String userId;
    private String title;
    private String description;

    private Integer durationInWeeks;
    private Integer targetCalories;
    private String planType;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private List<PlanDayResponse> days;
}
