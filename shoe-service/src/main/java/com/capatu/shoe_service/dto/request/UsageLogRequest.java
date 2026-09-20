package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.Constants;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UsageLogRequest {

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    private Long shoeId;

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    @PastOrPresent(message = Constants.VALIDATION_PAST_OR_PRESENT)
    private LocalDate activityDate;

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    @Positive(message = Constants.VALIDATION_POSITIVE)
    @Digits(integer = 4, fraction = 2, message = Constants.VALIDATION_DIGITS)
    private BigDecimal distanceKm;

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    private Long activityTypeRefId;

    @Positive(message = Constants.VALIDATION_POSITIVE)
    private Integer durationMinutes;

    private String notes;
}
