package com.capatu.shoe_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ShoeUsageSummary {

    Long getShoeId();

    String getShoeName();

    String getShoeType();

    Long getUsageCount();

    BigDecimal getTotalDistanceKm();

    BigDecimal getAvgDistanceKm();

    LocalDate getLastUsedAt();
}
