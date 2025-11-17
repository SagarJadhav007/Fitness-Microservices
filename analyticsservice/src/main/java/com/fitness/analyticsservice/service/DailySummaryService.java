package com.fitness.analyticsservice.service;

import com.fitness.analyticsservice.config.RabbitMqConfig;
import com.fitness.analyticsservice.dto.DailySummaryPayload;
import com.fitness.analyticsservice.model.ActivityMetric;
import com.fitness.analyticsservice.repository.ActivityMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailySummaryService {

    private final ActivityMetricRepository activityMetricRepository;
    private final PlanLookupService planLookupService;
    private final RabbitTemplate rabbitTemplate;

    public void generateForDate(LocalDate date) {

        // Simplest approach: fetch all users who had ANY activity today
        List<String> userIds = activityMetricRepository.findDistinctUserIds();

        for (String userId : userIds) {
            DailySummaryPayload summary = computeSummaryForUser(userId, date);

            rabbitTemplate.convertAndSend(
                    RabbitMqConfig.EXCHANGE_NAME,
                    RabbitMqConfig.DAILY_ROUTING_KEY,
                    summary
            );
        }
    }

    private DailySummaryPayload computeSummaryForUser(String userId, LocalDate date) {
        Instant start = date.atStartOfDay(ZoneId.of("Asia/Kolkata")).toInstant();
        Instant end   = date.plusDays(1).atStartOfDay(ZoneId.of("Asia/Kolkata")).toInstant();

        List<ActivityMetric> metrics =
                activityMetricRepository.findByUserIdAndActivityDateBetween(userId, start, end);

        int totalCalories = metrics.stream()
                .filter(m -> m.getActualCalories() != null)
                .mapToInt(ActivityMetric::getActualCalories)
                .sum();

        int totalMinutes = metrics.stream()
                .filter(m -> m.getActualDurationMinutes() != null)
                .mapToInt(ActivityMetric::getActualDurationMinutes)
                .sum();

        double adherence = metrics.stream()
                .filter(m -> m.getCompletionPercent() != null)
                .mapToDouble(ActivityMetric::getCompletionPercent)
                .average()
                .orElse(0.0) / 100.0;

        // Activity details
        List<Map<String,Object>> activities = metrics.stream().map(m -> {
            Map<String,Object> map = new HashMap<>();
            map.put("planned", m.getPlannedActivityType());
            map.put("actual", m.getActualActivityType());
            map.put("plannedCalories", m.getPlannedCalories());
            map.put("actualCalories", m.getActualCalories());
            map.put("plannedMinutes", m.getPlannedDurationMinutes());
            map.put("actualMinutes", m.getActualDurationMinutes());
            map.put("completion", m.getCompletionPercent());
            map.put("timestamp", m.getActivityDate());
            return map;
        }).collect(Collectors.toList());

        // Missed workouts
        List<String> planned = metrics.stream()
                .map(ActivityMetric::getPlannedActivityType)
                .filter(Objects::nonNull)
                .toList();

        List<String> actual = metrics.stream()
                .map(ActivityMetric::getActualActivityType)
                .filter(Objects::nonNull)
                .toList();

        List<String> missed = planned.stream()
                .filter(p -> !actual.contains(p))
                .toList();

        return DailySummaryPayload.builder()
                .userId(userId)
                .date(date)
                .totalCaloriesBurned(totalCalories)
                .totalMinutesActive(totalMinutes)
                .adherence(adherence)
                .activities(activities)
                .missedWorkouts(missed)
                .metadata(Map.of("source", "daily-aggregation"))
                .build();
    }
}
