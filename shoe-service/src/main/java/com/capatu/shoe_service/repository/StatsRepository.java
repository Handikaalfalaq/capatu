package com.capatu.shoe_service.repository;

import com.capatu.shoe_service.dto.response.ActivityLogRow;
import com.capatu.shoe_service.dto.response.IdleShoe;
import com.capatu.shoe_service.dto.response.ShoeLifespan;
import com.capatu.shoe_service.dto.response.ShoeRanking;
import com.capatu.shoe_service.dto.response.ShoeUsageSummary;
import com.capatu.shoe_service.entity.ShoeModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface StatsRepository extends Repository<ShoeModel, Long> {

    @Query(value = """
            SELECT s.id                                          AS shoeId,
                   s.name                                        AS shoeName,
                   t.code_name                                   AS shoeType,
                   COUNT(u.id)                                   AS usageCount,
                   COALESCE(SUM(u.distance_km), 0)               AS totalDistanceKm,
                   ROUND(COALESCE(AVG(u.distance_km), 0), 2)     AS avgDistanceKm,
                   MAX(u.activity_date)                          AS lastUsedAt
            FROM shoe s
            JOIN ref_code t ON t.id = s.shoe_type_ref_id
            LEFT JOIN usage_log u ON u.shoe_id = s.id AND u.activity_date BETWEEN :from AND :to
            GROUP BY s.id, s.name, t.code_name
            ORDER BY totalDistanceKm DESC, s.name
            """, nativeQuery = true)
    List<ShoeUsageSummary> summarizeByShoe(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = """
            SELECT s.id                                          AS shoeId,
                   s.name                                        AS shoeName,
                   COUNT(u.id)                                   AS usageCount,
                   RANK() OVER (ORDER BY COUNT(u.id) DESC)       AS mostUsedRank,
                   RANK() OVER (ORDER BY COUNT(u.id) ASC)        AS leastUsedRank
            FROM shoe s
            LEFT JOIN usage_log u ON u.shoe_id = s.id AND u.activity_date BETWEEN :from AND :to
            WHERE s.retired = FALSE
            GROUP BY s.id, s.name
            ORDER BY mostUsedRank, s.name
            """, nativeQuery = true)
    List<ShoeRanking> rankShoes(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = """
            SELECT s.id           AS shoeId,
                   s.name         AS shoeName,
                   s.last_used_at AS lastUsedAt
            FROM shoe s
            WHERE s.retired = FALSE
              AND NOT EXISTS (SELECT 1 FROM usage_log u WHERE u.shoe_id = s.id AND u.activity_date >= :since)
            ORDER BY s.last_used_at NULLS FIRST, s.name
            """, nativeQuery = true)
    List<IdleShoe> findIdleShoes(@Param("since") LocalDate since);

    @Query(value = """
            SELECT s.id                                                                                       AS shoeId,
                   s.name                                                                                     AS shoeName,
                   t.code_name                                                                                AS shoeType,
                   s.total_distance_km                                                                        AS totalDistanceKm,
                   s.target_lifespan_km                                                                       AS targetLifespanKm,
                   ROUND((s.target_lifespan_km - s.total_distance_km) * 100.0 / s.target_lifespan_km, 1)      AS remainingPercent,
                   STRING_AGG(f.code_name, ', ' ORDER BY f.code_name)                                         AS features
            FROM shoe s
            JOIN ref_code t ON t.id = s.shoe_type_ref_id
            LEFT JOIN shoe_feature_map m ON m.shoe_id = s.id
            LEFT JOIN ref_code f ON f.id = m.feature_ref_id
            WHERE s.retired = FALSE AND s.target_lifespan_km IS NOT NULL
            GROUP BY s.id, s.name, t.code_name, s.total_distance_km, s.target_lifespan_km
            HAVING (s.target_lifespan_km - s.total_distance_km) * 100.0 / s.target_lifespan_km < :threshold
            ORDER BY remainingPercent, s.name
            """, nativeQuery = true)
    List<ShoeLifespan> findShoeLifespans(@Param("threshold") BigDecimal threshold);

    @Query(value = """
            SELECT u.activity_date AS activityDate,
                   u.distance_km   AS distanceKm,
                   a.code_name     AS activity
            FROM usage_log u
            JOIN ref_code a ON a.id = u.activity_type_ref_id
            WHERE u.activity_date BETWEEN :from AND :to
            ORDER BY u.activity_date, u.id
            """, nativeQuery = true)
    List<ActivityLogRow> findActivityRows(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
