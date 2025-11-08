package com.fitness.userservice.dto;

import com.fitness.userservice.model.ActivityType;
import lombok.Data;

@Data
public class PlanDayRequest {

    private Integer dayNumber;
    private ActivityType activityType;
    private Integer targetCalories;
    private Integer durationMinutes;
    private String intensity;
    private String notes;
}
