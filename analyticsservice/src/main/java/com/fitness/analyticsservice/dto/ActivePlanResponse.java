package com.fitness.analyticsservice.dto;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class ActivePlanResponse {
    private String id;
    private List<PlanDayData> days;

    @Data
    public static class PlanDayData {
        private Integer dayNumber;
        private String activityType;
        private Integer targetCalories;
        private Integer durationMinutes;
    }
}
