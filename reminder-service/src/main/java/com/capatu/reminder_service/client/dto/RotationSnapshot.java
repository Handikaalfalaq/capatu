package com.capatu.reminder_service.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RotationSnapshot(int days, List<Share> shares) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Share(Long shoeId, String shoeName, long usageCount, BigDecimal percent, boolean unhealthy) {
    }
}
