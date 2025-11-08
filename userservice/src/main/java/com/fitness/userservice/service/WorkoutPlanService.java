package com.fitness.userservice.service;

import com.fitness.userservice.dto.CreateWorkoutPlanRequest;
import com.fitness.userservice.dto.PlanDayResponse;
import com.fitness.userservice.dto.WorkoutPlanResponse;
import com.fitness.userservice.model.PlanDay;
import com.fitness.userservice.model.PlanType;
import com.fitness.userservice.model.WorkoutPlan;
import com.fitness.userservice.repository.PlanDayRepository;
import com.fitness.userservice.repository.WorkoutPlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository planRepository;
    private final PlanDayRepository planDayRepository;

    public WorkoutPlanResponse createPlan(String userId, CreateWorkoutPlanRequest request) {

        WorkoutPlan plan = new WorkoutPlan();
        plan.setUserId(userId);
        plan.setTitle(request.getTitle());
        plan.setDescription(request.getDescription());
        plan.setDurationInWeeks(request.getDurationInWeeks());
        plan.setTargetCalories(request.getTargetCalories());
        plan.setPlanType(PlanType.valueOf(request.getPlanType()));

        WorkoutPlan savedPlan = planRepository.save(plan);

        // Save days
        List<PlanDay> days = request.getDays().stream().map(dto -> {
            PlanDay day = new PlanDay();
            day.setPlanId(savedPlan.getId());
            day.setDayNumber(dto.getDayNumber());
            day.setActivityType(dto.getActivityType());
            day.setTargetCalories(dto.getTargetCalories());
            day.setDurationMinutes(dto.getDurationMinutes());
            day.setIntensity(dto.getIntensity());
            day.setNotes(dto.getNotes());
            return day;
        }).toList();

        planDayRepository.saveAll(days);

        return buildPlanResponse(savedPlan);
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
        resp.setPlanType(plan.getPlanType().name());
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
