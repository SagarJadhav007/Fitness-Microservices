package com.fitness.userservice.service;

import com.fitness.userservice.dto.PlanDayResponse;
import com.fitness.userservice.dto.SaveWorkoutPlanRequest;
import com.fitness.userservice.dto.WorkoutPlanResponse;
import com.fitness.userservice.model.PlanDay;
import com.fitness.userservice.model.WorkoutPlan;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.PlanDayRepository;
import com.fitness.userservice.repository.WorkoutPlanRepository;
import com.fitness.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository planRepository;
    private final PlanDayRepository planDayRepository;
    private final UserRepository userRepository;

    /**
     * This is the *only* plan saving function.
     * Called after user customizes AI-generated plan.
     */
    public WorkoutPlanResponse savePlan(String userId, SaveWorkoutPlanRequest req) {

        WorkoutPlan plan = new WorkoutPlan();
        plan.setUserId(userId);
        plan.setTitle(req.getTitle());
        plan.setDescription(req.getDescription());
        plan.setDurationInWeeks(req.getDurationInWeeks());
        plan.setTargetCalories(req.getTargetCalories());
        plan.setPlanType(req.getPlanType());
        plan.setIsActive(true);

        WorkoutPlan saved = planRepository.save(plan);

        req.getDays().forEach(d -> {
            PlanDay day = new PlanDay();
            day.setPlanId(saved.getId());
            day.setDayNumber(d.getDayNumber());
            day.setActivityType(d.getActivityType());
            day.setTargetCalories(d.getTargetCalories());
            day.setDurationMinutes(d.getDurationMinutes());
            day.setIntensity(d.getIntensity());
            day.setNotes(d.getNotes());
            planDayRepository.save(day);
        });

        User user = userRepository.findById(userId).orElseThrow();
        user.setActivePlanId(saved.getId());
        userRepository.save(user);

        return buildPlanResponse(saved);
    }

    public WorkoutPlanResponse getPlan(String planId) {
        WorkoutPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        return buildPlanResponse(plan);
    }

    public List<WorkoutPlanResponse> getPlansByUser(String userId) {
        return planRepository.findByUserId(userId)
                .stream()
                .map(this::buildPlanResponse)
                .toList();
    }

    private WorkoutPlanResponse buildPlanResponse(WorkoutPlan plan) {
        WorkoutPlanResponse resp = new WorkoutPlanResponse();

        resp.setId(plan.getId());
        resp.setUserId(plan.getUserId());
        resp.setTitle(plan.getTitle());
        resp.setDescription(plan.getDescription());
        resp.setDurationInWeeks(plan.getDurationInWeeks());
        resp.setTargetCalories(plan.getTargetCalories());
        resp.setPlanType(plan.getPlanType());
        resp.setIsActive(plan.getIsActive());
        resp.setCreatedAt(plan.getCreatedAt());

        List<PlanDayResponse> dayResponses = planDayRepository.findByPlanId(plan.getId())
                .stream().map(day -> {
                    PlanDayResponse d = new PlanDayResponse();
                    d.setId(day.getId());
                    d.setDayNumber(day.getDayNumber());
                    d.setActivityType(day.getActivityType());
                    d.setTargetCalories(day.getTargetCalories());
                    d.setDurationMinutes(day.getDurationMinutes());
                    d.setIntensity(day.getIntensity());
                    d.setNotes(day.getNotes());
                    return d;
                }).toList();

        resp.setDays(dayResponses);
        return resp;
    }
}
