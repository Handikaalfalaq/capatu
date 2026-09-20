package com.capatu.shoe_service.dto.response;

import java.math.BigDecimal;

public interface ShoeLifespan {

    Long getShoeId();

    String getShoeName();

    String getShoeType();

    BigDecimal getTotalDistanceKm();

    Integer getTargetLifespanKm();

    BigDecimal getRemainingPercent();

    String getFeatures();
}
