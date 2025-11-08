package com.fitness.userservice.repository;

import com.fitness.userservice.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, String> {

    List<WorkoutPlan> findByUserId(String userId);

}

