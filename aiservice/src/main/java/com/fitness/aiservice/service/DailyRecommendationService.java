package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.dto.DailySummaryPayload;
import com.fitness.aiservice.model.DailyRecommendation;
import com.fitness.aiservice.repository.DailyRecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DailyRecommendationService {

    private final GeminiService geminiService;
    private final DailyRecommendationRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public void generateRecommendation(DailySummaryPayload payload) {

        String prompt = buildPrompt(payload);
        log.info("Sending Daily Prompt to Gemini:\n{}", prompt);

        String aiResponse = geminiService.getAnswer(prompt);
        log.info("RAW GEMINI DAILY RESPONSE: {}", aiResponse);

        try {
            String extractedText = extractTextFromGemini(aiResponse);

            DailyRecommendation rec = DailyRecommendation.builder()
                    .userId(payload.getUserId())
                    .date(payload.getDate())
                    .recommendationText(extractedText)
                    .motivationLine(extractMotivation(extractedText))
                    .structuredAdvice(extractStructuredJson(extractedText))
                    .createdAt(System.currentTimeMillis())
                    .build();

            repository.save(rec);

        } catch (Exception e) {
            log.error("Failed to parse Gemini daily summary output", e);
        }
    }

    /** Build Prompt DIRECTLY HERE (NO PromptFactory) */
    private String buildPrompt(DailySummaryPayload d) {

        return """
                You are a certified AI fitness coach. Below is the user's DAILY WORKOUT SUMMARY.

                USER: %s
                DATE: %s

                TOTAL CALORIES: %d
                TOTAL ACTIVE MINUTES: %d
                ADHERENCE SCORE (0-1): %.2f
                MISSED WORKOUTS: %s

                ACTIVITIES (planned vs actual):
                %s

                YOUR TASK:
                1. Write a 3–4 line clear summary of the day.
                2. Suggest 2–3 improvements for tomorrow.
                3. Provide ONE short motivational line (max 10 words).
                4. Provide a PURE JSON object like this:

                {
                  "focusArea": "...",
                  "tomorrowWorkout": [
                      {"name": "...", "minutes": 30, "intensity": "moderate"}
                  ],
                  "nutritionTip": "...",
                  "hydrationGoalLiters": 2.5
                }

                OUTPUT FORMAT RULES (VERY IMPORTANT):

                ---SUMMARY---
                <summary_text>

                ---ADVICE---
                <recommendations>

                ---MOTIVATION---
                <short_line>

                ---STRUCTURED_JSON---
                <json_only>
                """.formatted(
                d.getUserId(),
                d.getDate(),
                d.getTotalCaloriesBurned(),
                d.getTotalMinutesActive(),
                d.getAdherence(),
                d.getMissedWorkouts(),
                d.getActivities()
        );
    }

    /** Same parsing style as your WorkoutPlanAIService */
    private String extractTextFromGemini(String aiResponse) {
        try {
            JsonNode root = mapper.readTree(aiResponse);
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            log.error("Failed to extract text, returning raw output", e);
            return aiResponse;
        }
    }

    private String extractMotivation(String text) {
        try {
            if (text.contains("---MOTIVATION---")) {
                return text.split("---MOTIVATION---")[1]
                        .split("---")[0]
                        .trim();
            }
        } catch (Exception ignore) {}
        return "";
    }

    private Map<String, Object> extractStructuredJson(String text) {
        try {
            if (!text.contains("---STRUCTURED_JSON---")) return Map.of();

            String json = text.split("---STRUCTURED_JSON---")[1].trim();
            json = json.replace("```json", "")
                    .replace("```", "")
                    .trim();

            return mapper.readValue(json, Map.class);

        } catch (Exception e) {
            log.error("Failed to parse structured JSON", e);
            return Map.of("raw", text);
        }
    }
}
