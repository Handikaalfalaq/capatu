package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.Constants;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UsageLogSearchRequest extends PageableRequest {

    private Long shoeId;
    private Long activityTypeRefId;

    private LocalDate activityDateFrom;
    private LocalDate activityDateTo;

    @PositiveOrZero(message = Constants.VALIDATION_POSITIVE_OR_ZERO)
    private BigDecimal distanceKmFrom;
    @PositiveOrZero(message = Constants.VALIDATION_POSITIVE_OR_ZERO)
    private BigDecimal distanceKmTo;

    @PositiveOrZero(message = Constants.VALIDATION_POSITIVE_OR_ZERO)
    private Integer durationMinutesFrom;
    @PositiveOrZero(message = Constants.VALIDATION_POSITIVE_OR_ZERO)
    private Integer durationMinutesTo;
}
