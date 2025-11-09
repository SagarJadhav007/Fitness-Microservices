package com.fitness.aiservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class PlanDraftResponse {
    private String title;
    private String description;
    private Integer durationInWeeks;
    private Integer targetCalories;
    private String planType;
    private List<PlanDayData> days;

    @Data
    public static class PlanDayData {
        private Integer dayNumber;
        private String activityType;
        private Integer targetCalories;
        private Integer durationMinutes;
        private String intensity;
        private String notes;
    }
}
