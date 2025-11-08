package com.fitness.userservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "plan_days")
@Data
public class PlanDay {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String planId;

    private Integer dayNumber; // e.g., day 1, day 2, day 3...

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;  // RUNNING, CYCLING, WALKING, GYM etc.

    private Integer targetCalories;     // calories to burn that day
    private Integer durationMinutes;    // workout duration
    private String intensity;           // LOW, MEDIUM, HIGH

    private String notes;               // additional AI advice for that day
}

