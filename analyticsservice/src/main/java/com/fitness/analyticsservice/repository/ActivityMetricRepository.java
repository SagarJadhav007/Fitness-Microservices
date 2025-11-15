package com.fitness.analyticsservice.repository;

import com.fitness.analyticsservice.model.ActivityMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityMetricRepository extends JpaRepository<ActivityMetric, Long> {}
