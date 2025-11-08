package com.fitness.userservice.repository;

import com.fitness.userservice.model.PlanDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanDayRepository extends JpaRepository<PlanDay, String> {
    List<PlanDay> findByPlanId(String planId);
}
