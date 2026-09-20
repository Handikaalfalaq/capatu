package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.Constants;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class ShoeRequest {

    @NotBlank(message = Constants.VALIDATION_REQUIRED)
    @Size(max = 100, message = Constants.VALIDATION_MAX_LENGTH)
    private String name;

    @NotBlank(message = Constants.VALIDATION_REQUIRED)
    @Size(max = 100, message = Constants.VALIDATION_MAX_LENGTH)
    private String brand;

    @NotBlank(message = Constants.VALIDATION_REQUIRED)
    @Size(max = 100, message = Constants.VALIDATION_MAX_LENGTH)
    private String model;

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    private Long shoeTypeRefId;

    @NotBlank(message = Constants.VALIDATION_REQUIRED)
    @Size(max = 50, message = Constants.VALIDATION_MAX_LENGTH)
    private String color;

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    @Positive(message = Constants.VALIDATION_POSITIVE)
    @Digits(integer = 3, fraction = 1, message = Constants.VALIDATION_DIGITS)
    private BigDecimal sizeValue;

    @NotBlank(message = Constants.VALIDATION_REQUIRED)
    @Pattern(regexp = Constants.SIZE_SYSTEM_FORMAT, message = Constants.VALIDATION_SIZE_SYSTEM)
    private String sizeSystem;

    @Positive(message = Constants.VALIDATION_POSITIVE)
    private Integer weightGrams;

    @NotNull(message = Constants.VALIDATION_REQUIRED)
    @PastOrPresent(message = Constants.VALIDATION_PAST_OR_PRESENT)
    private LocalDate purchaseDate;

    @PositiveOrZero(message = Constants.VALIDATION_POSITIVE_OR_ZERO)
    @Digits(integer = 10, fraction = 2, message = Constants.VALIDATION_DIGITS)
    private BigDecimal purchasePrice;

    @Positive(message = Constants.VALIDATION_POSITIVE)
    private Integer targetLifespanKm;

    private Set<Long> featureRefIds;
}
