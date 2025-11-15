package com.fitness.analyticsservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "activity_metrics")
public class ActivityMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String planId;

    private Instant activityDate;

    private String plannedActivityType;
    private String actualActivityType;

    private Integer plannedCalories;
    private Integer actualCalories;

    private Integer plannedDurationMinutes;
    private Integer actualDurationMinutes;

    @Column(name = "completion_percent", insertable = false, updatable = false)
    private Double completionPercent;
}
