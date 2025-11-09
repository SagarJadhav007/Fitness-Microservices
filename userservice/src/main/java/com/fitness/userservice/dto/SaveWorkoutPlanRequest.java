package com.fitness.userservice.dto;

import com.fitness.userservice.model.ActivityType;
import com.fitness.userservice.model.PlanType;
import lombok.Data;
import java.util.List;

@Data
public class SaveWorkoutPlanRequest {
    private String title;
    private String description;
    private Integer durationInWeeks;
    private Integer targetCalories;
    private PlanType planType;
    private List<PlanDayData> days;

    @Data
    public static class PlanDayData {
        private Integer dayNumber;
        private ActivityType activityType;
        private Integer targetCalories;
        private Integer durationMinutes;
        private String intensity;
        private String notes;
    }
}
