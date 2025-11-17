package com.fitness.aiservice.service;

import com.fitness.aiservice.config.RabbitMqConfig;
import com.fitness.aiservice.dto.DailySummaryPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailySummaryListener {

    private final DailyRecommendationService dailyRecService;

    @RabbitListener(queues = RabbitMqConfig.DAILY_SUMMARY_QUEUE)
    public void onDailySummary(DailySummaryPayload payload) {
        dailyRecService.generateRecommendation(payload);
    }
}
