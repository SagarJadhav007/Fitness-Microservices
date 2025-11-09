package com.fitness.userservice.dto;

import com.fitness.userservice.model.PlanType;
import lombok.Data;
import java.util.List;

@Data
public class CreateWorkoutPlanRequest {

    private String title;
    private String description;

    private Integer durationInWeeks;
    private Integer targetCalories;

    private PlanType planType; // or PlanType enum if you want

    private List<PlanDayRequest> days;
}
