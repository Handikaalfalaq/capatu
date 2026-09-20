package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.MessageConstants;
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

/**
 * Data yang boleh dikirim client saat menambah atau mengubah sepatu.
 * Kolom retired, retired_at, agregat pemakaian, dan audit sengaja tidak ada di sini (diatur sistem).
 */
@Getter
@Setter
public class ShoeRequest {

    @NotBlank(message = MessageConstants.VALIDATION_REQUIRED)
    @Size(max = 100, message = MessageConstants.VALIDATION_MAX_LENGTH)
    private String name;

    @NotBlank(message = MessageConstants.VALIDATION_REQUIRED)
    @Size(max = 100, message = MessageConstants.VALIDATION_MAX_LENGTH)
    private String brand;

    @NotBlank(message = MessageConstants.VALIDATION_REQUIRED)
    @Size(max = 100, message = MessageConstants.VALIDATION_MAX_LENGTH)
    private String model;

    @NotNull(message = MessageConstants.VALIDATION_REQUIRED)
    private Long shoeTypeRefId;

    @NotBlank(message = MessageConstants.VALIDATION_REQUIRED)
    @Size(max = 50, message = MessageConstants.VALIDATION_MAX_LENGTH)
    private String color;

    @NotNull(message = MessageConstants.VALIDATION_REQUIRED)
    @Positive(message = MessageConstants.VALIDATION_POSITIVE)
    @Digits(integer = 3, fraction = 1, message = MessageConstants.VALIDATION_DIGITS)
    private BigDecimal sizeValue;

    @NotBlank(message = MessageConstants.VALIDATION_REQUIRED)
    @Pattern(regexp = MessageConstants.SIZE_SYSTEM_FORMAT, message = MessageConstants.VALIDATION_SIZE_SYSTEM)
    private String sizeSystem;

    @Positive(message = MessageConstants.VALIDATION_POSITIVE)
    private Integer weightGrams;

    @NotNull(message = MessageConstants.VALIDATION_REQUIRED)
    @PastOrPresent(message = MessageConstants.VALIDATION_PAST_OR_PRESENT)
    private LocalDate purchaseDate;

    @PositiveOrZero(message = MessageConstants.VALIDATION_POSITIVE_OR_ZERO)
    @Digits(integer = 10, fraction = 2, message = MessageConstants.VALIDATION_DIGITS)
    private BigDecimal purchasePrice;

    @Positive(message = MessageConstants.VALIDATION_POSITIVE)
    private Integer targetLifespanKm;

    private Set<Long> featureRefIds;
}
