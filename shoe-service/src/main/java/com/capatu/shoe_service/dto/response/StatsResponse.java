package com.capatu.shoe_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class StatsResponse {

    private StatsResponse() {
    }

    public record Summary(LocalDate from, LocalDate to, BigDecimal totalDistanceKm,
                          List<ShoeUsage> shoes, List<ActivityBreakdown> byActivity) {
    }

    public record ShoeUsage(Long shoeId, String shoeName, String shoeType, long usageCount,
                            BigDecimal totalDistanceKm, BigDecimal avgDistanceKm, LocalDate lastUsedAt) {
    }

    public record ActivityBreakdown(String activity, BigDecimal distanceKm, BigDecimal percent) {
    }

    public record Ranking(LocalDate from, LocalDate to, List<ShoeRank> shoes) {
    }

    public record ShoeRank(Long shoeId, String shoeName, long usageCount, long mostUsedRank, long leastUsedRank) {
    }

    public record Rotation(int days, LocalDate from, LocalDate to,
                           List<RotationShare> shares, List<IdleShoeItem> idleShoes) {
    }

    public record RotationShare(Long shoeId, String shoeName, long usageCount, BigDecimal percent, boolean unhealthy) {
    }

    public record IdleShoeItem(Long shoeId, String shoeName, LocalDate lastUsedAt) {
    }

    public record ShoeLife(BigDecimal threshold, List<ShoeLifeItem> shoes) {
    }

    public record ShoeLifeItem(Long shoeId, String shoeName, String shoeType, BigDecimal totalDistanceKm,
                               Integer targetLifespanKm, BigDecimal remainingPercent, String features) {
    }
}
