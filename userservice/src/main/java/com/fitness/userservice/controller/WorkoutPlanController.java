package com.fitness.userservice.controller;

import com.fitness.userservice.dto.CreateWorkoutPlanRequest;
import com.fitness.userservice.dto.WorkoutPlanResponse;
import com.fitness.userservice.service.WorkoutPlanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@AllArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService service;

    @PostMapping("/{userId}")
    public ResponseEntity<WorkoutPlanResponse> createPlan(
            @PathVariable String userId,
            @RequestBody CreateWorkoutPlanRequest request
    ) {
        return ResponseEntity.ok(service.createPlan(userId, request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WorkoutPlanResponse>> getPlans(@PathVariable String userId) {
        return ResponseEntity.ok(service.getPlansByUser(userId));
    }

    @GetMapping("/details/{planId}")
    public ResponseEntity<WorkoutPlanResponse> getPlan(@PathVariable String planId) {
        return ResponseEntity.ok(service.getPlan(planId));
    }
}

