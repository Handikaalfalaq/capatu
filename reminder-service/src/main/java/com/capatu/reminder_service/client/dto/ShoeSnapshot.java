package com.capatu.reminder_service.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShoeSnapshot(Long id, String name, Boolean retired, BigDecimal totalDistanceKm,
                           Integer targetLifespanKm, Integer usageCountSinceLastWash) {

    public boolean isActive() {
        return !Boolean.TRUE.equals(retired);
    }
}
