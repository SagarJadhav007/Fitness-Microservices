package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.dto.GoalsRequest;
import com.fitness.aiservice.dto.PlanDraftResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkoutPlanAIService {

    private final GeminiService geminiService;

    public PlanDraftResponse generateDraft(GoalsRequest goals) {
        String prompt = createPrompt(goals);

        String aiResponse = geminiService.getAnswer(prompt);
        log.info("AI RAW RESPONSE: {}", aiResponse);

        return processAiResponse(goals, aiResponse);
    }

    private PlanDraftResponse processAiResponse(GoalsRequest goals, String aiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\\n```", "")
                    .trim();

            log.info("PARSED JSON BLOCK: {}", jsonContent);

            return mapper.readValue(jsonContent, PlanDraftResponse.class);

        } catch (Exception e) {
            log.error("Workout Plan Parsing Failed. Returning default.", e);
            return createDefaultPlan(goals);
        }
    }

    private PlanDraftResponse createDefaultPlan(GoalsRequest goals) {
        PlanDraftResponse plan = new PlanDraftResponse();
        plan.setTitle("Basic " + goals.getGoalType() + " Plan");
        plan.setDescription("Unable to generate AI-based detailed plan. Here is a simple starter plan.");
        plan.setDurationInWeeks(4);
        plan.setTargetCalories(goals.getTargetCalories());
        plan.setPlanType(goals.getGoalType());
        plan.setDays(Collections.emptyList());
        return plan;
    }

    private String createPrompt(GoalsRequest goals) {
        return String.format("""
                        You are a fitness coach AI.
                        Generate a workout plan in STRICT JSON format with the following structure:
                        
                        {
                          "title": "string",
                          "description": "string",
                          "durationInWeeks": number,
                          "targetCalories": number,
                          "planType": "string",
                          "days": [
                            {
                              "dayNumber": number,
                              "activityType": "string",
                              "targetCalories": number,
                              "durationMinutes": number,
                              "intensity": "string",
                              "notes": "string"
                            }
                          ]
                        }
                        
                        VERY IMPORTANT ENUM RULES:
                        - planType must be EXACTLY one of:
                          ["BEGINNER","WEIGHT_LOSS","ENDURANCE","MUSCLE_GAIN","GENERAL"]
                        - activityType must be EXACTLY one of:
                          ["RUNNING","WALKING","CYCLING","SWIMMING","WEIGHT_TRAINING","YOGA","CARDIO","STRETCHING","HIIT","OTHER"]
                        - Use EXACT uppercase values as listed above. No new or custom values.
                        
                        DURATION AND DAY LOGIC:
                        - Use a reasonable workout structure based on the user's goal.
                        - Include rest / recovery days when appropriate.
                        - Ensure the number of days and intensity logically align with the planType. Example:- If the plan implies a weekly routine, generate 7 days.
                        
                        OUTPUT RULES:
                        - Do NOT include any explanation, reasoning, or comments.
                        - Respond ONLY with valid JSON. No markdown code blocks.
                        
                        User Goal: %s
                        Target Calories per day: %s
                        """,
                goals.getGoalType(),
                goals.getTargetCalories()
        );
    }
}

