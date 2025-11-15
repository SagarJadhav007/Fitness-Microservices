package com.fitness.analyticsservice.messaging;

import com.fitness.analyticsservice.dto.ActivityLogMessage;
import com.fitness.analyticsservice.model.ActivityMetric;
import com.fitness.analyticsservice.repository.ActivityMetricRepository;
import com.fitness.analyticsservice.service.PlanLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ActivityLogConsumer {

    private final ActivityMetricRepository repository;
    private final PlanLookupService planLookupService;

    @RabbitListener(queues = "analytics.queue")
    public void consume(ActivityLogMessage msg) {

        var planned = planLookupService.getPlannedDayForUser(msg.getUserId(), msg.getActivityType());

        ActivityMetric metric = new ActivityMetric();
        metric.setUserId(msg.getUserId());
        metric.setPlanId(planned.planId());
        metric.setActivityDate(Instant.ofEpochMilli(msg.getTimestamp()));

        metric.setPlannedActivityType(planned.activityType());
        metric.setActualActivityType(msg.getActivityType());

        metric.setPlannedCalories(planned.targetCalories());
        metric.setActualCalories(msg.getCaloriesBurned());

        metric.setPlannedDurationMinutes(planned.durationMinutes());
        metric.setActualDurationMinutes(msg.getDurationMinutes());

        repository.save(metric);
    }
}
