package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MaintenanceLogRequest {

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    private Long shoeId;

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    private Long actionRefId;

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    @PastOrPresent(message = Constants.VALIDATION_PAST_OR_PRESENT)
    private LocalDate actionDate;

    private String notes;
}
