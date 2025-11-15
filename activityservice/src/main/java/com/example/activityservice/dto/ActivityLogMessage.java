package com.example.activityservice.dto;

import lombok.Data;

@Data
public class ActivityLogMessage {
    private String userId;
    private String activityType;
    private Integer durationMinutes;
    private Integer caloriesBurned;
    private long timestamp;
}
