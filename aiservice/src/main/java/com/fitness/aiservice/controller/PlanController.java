package com.fitness.aiservice.controller;

import com.fitness.aiservice.dto.GoalsRequest;
import com.fitness.aiservice.dto.PlanDraftResponse;
import com.fitness.aiservice.service.WorkoutPlanAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/plans")
@RequiredArgsConstructor
public class PlanController {

    private final WorkoutPlanAIService workoutPlanAIService;

    @PostMapping("/generate")
    public PlanDraftResponse generate(@RequestBody GoalsRequest request) {
        return workoutPlanAIService.generateDraft(request);
    }
}
