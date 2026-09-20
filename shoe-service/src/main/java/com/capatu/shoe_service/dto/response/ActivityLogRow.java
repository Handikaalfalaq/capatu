package com.capatu.shoe_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ActivityLogRow {

    LocalDate getActivityDate();

    BigDecimal getDistanceKm();

    String getActivity();
}
