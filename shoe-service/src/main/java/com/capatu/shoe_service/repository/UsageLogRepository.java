package com.capatu.shoe_service.repository;

import com.capatu.shoe_service.dto.response.UsageLogSummary;
import com.capatu.shoe_service.entity.UsageLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsageLogRepository extends JpaRepository<UsageLogModel, Long>, JpaSpecificationExecutor<UsageLogModel> {

    @Query(value = """
            SELECT COALESCE(SUM(u.distance_km), 0)                                                        AS totalDistanceKm,
                   COUNT(*)                                                                               AS usageCount,
                   MAX(u.activity_date)                                                                   AS lastUsedAt,
                   COUNT(*) FILTER (WHERE s.last_washed_at IS NULL OR u.activity_date > s.last_washed_at) AS usageCountSinceLastWash
            FROM usage_log u
            JOIN shoe s ON s.id = u.shoe_id
            WHERE u.shoe_id = :shoeId
            """, nativeQuery = true)
    UsageLogSummary summarize(@Param("shoeId") Long shoeId);
}
