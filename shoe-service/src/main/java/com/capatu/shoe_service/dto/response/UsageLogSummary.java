package com.capatu.shoe_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UsageLogSummary {

    BigDecimal getTotalDistanceKm();

    Long getUsageCount();

    LocalDate getLastUsedAt();

    Long getUsageCountSinceLastWash();
}
