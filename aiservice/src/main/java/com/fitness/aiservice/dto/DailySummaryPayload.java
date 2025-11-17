package com.fitness.aiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySummaryPayload {

    private String userId;
    private LocalDate date;

    private int totalCaloriesBurned;
    private int totalMinutesActive;
    private double adherence;

    private List<Map<String, Object>> activities;
    private List<String> missedWorkouts;

    private Map<String, Object> metadata;
}
