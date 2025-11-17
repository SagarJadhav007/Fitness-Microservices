package com.fitness.analyticsservice.repository;

import com.fitness.analyticsservice.model.ActivityMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface ActivityMetricRepository extends JpaRepository<ActivityMetric, Long> {

    @Query(value = "SELECT DISTINCT user_id FROM activity_metrics", nativeQuery = true)
    List<String> findDistinctUserIds();

    List<ActivityMetric> findByUserIdAndActivityDateBetween(
            String userId,
            Instant start,
            Instant end
    );
}
