package com.fitness.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class DailySummaryScheduler {

    private final DailySummaryService dailySummaryService;

    @Scheduled(cron = "0 59 23 * * *", zone = "Asia/Kolkata")
    public void runDailySummary() {
        LocalDate date = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        dailySummaryService.generateForDate(date);
    }
}

