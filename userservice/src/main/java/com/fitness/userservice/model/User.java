package com.fitness.userservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;

@Entity
@Table(name ="users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true , nullable = false)
    private String email;

    private String keycloakId;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    // -----------------------------
    // FITNESS GOALS
    // -----------------------------
    private Integer dailyCaloriesGoal;             // e.g., 500
    private Integer weeklyWorkoutMinutesGoal;      // e.g., 150
    private Double targetWeight;                   // optional

    @Enumerated(EnumType.STRING)
    private PlanType planType;  // BEGINNER, WEIGHT_LOSS, ENDURANCE, MUSCLE_GAIN, GENERAL

    // Stores the ID of the active workout plan
    private String activePlanId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
