package com.fitness.userservice.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "workout_plans")
@Data
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String title;
    private String description;

    private Integer durationInWeeks;
    private Integer targetCalories;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    private Boolean isActive = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

