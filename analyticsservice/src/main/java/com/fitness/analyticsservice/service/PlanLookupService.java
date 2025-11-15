package com.fitness.analyticsservice.service;

import com.fitness.analyticsservice.dto.ActivePlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class PlanLookupService {

    private final WebClient.Builder webClientBuilder;

    public PlannedDayInfo getPlannedDayForUser(String userId, String actualActivityType) {

        ActivePlanResponse plan = webClientBuilder
                .build()
                .get()
                .uri("http://USER-SERVICE/api/plans/active/" + userId)
                .retrieve()
                .bodyToMono(ActivePlanResponse.class)
                .block();

        return plan.getDays().stream()
                .filter(d -> d.getActivityType().equalsIgnoreCase(actualActivityType))
                .findFirst()
                .map(d -> new PlannedDayInfo(plan.getId(), d.getActivityType(), d.getTargetCalories(), d.getDurationMinutes()))
                .orElse(new PlannedDayInfo(plan.getId(), actualActivityType, 0, 0));
    }

    public record PlannedDayInfo(String planId, String activityType, Integer targetCalories, Integer durationMinutes) {}
}
