package com.fitness.aiservice.repository;

import com.fitness.aiservice.model.DailyRecommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyRecommendationRepository extends MongoRepository<DailyRecommendation, String> {

    Optional<DailyRecommendation> findTopByUserIdAndDateOrderByCreatedAtDesc(
            String userId, LocalDate date
    );
}
