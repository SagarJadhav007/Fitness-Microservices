package com.fitness.aiservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "daily_recommendations")
public class DailyRecommendation {

    @Id
    private String id;

    private String userId;
    private LocalDate date;

    private String recommendationText;   // Full AI message
    private String motivationLine;       // Short motivational sentence

    private Map<String, Object> structuredAdvice; // optional structured JSON

    private long createdAt;
}
