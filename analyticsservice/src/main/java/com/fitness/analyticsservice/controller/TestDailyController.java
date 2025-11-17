package com.fitness.analyticsservice.controller;

import com.fitness.analyticsservice.service.DailySummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestDailyController {

    private final DailySummaryService dailySummaryService;

    @GetMapping("/daily-summary")
    public String sendDailySummary() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        dailySummaryService.generateForDate(today);
        return "Daily summary triggered for " + today;
    }
}
