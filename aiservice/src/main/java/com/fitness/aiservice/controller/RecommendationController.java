package com.fitness.aiservice.controller;

import com.fitness.aiservice.model.DailyRecommendation;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.DailyRecommendationRepository;
import com.fitness.aiservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final DailyRecommendationRepository repository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendation(@PathVariable String userId){
        return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendation(@PathVariable String activityId){
        return ResponseEntity.ok(recommendationService.getActivityRecommendation(activityId));
    }

    @GetMapping("/{userId}")
    public DailyRecommendation getTodayRecommendation(@PathVariable String userId) {
        LocalDate today = LocalDate.now();
        return repository.findTopByUserIdAndDateOrderByCreatedAtDesc(userId, today)
                .orElse(null);
    }
}
