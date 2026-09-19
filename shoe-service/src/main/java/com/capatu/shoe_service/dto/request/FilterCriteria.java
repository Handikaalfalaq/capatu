package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.MessageConstants;
import com.capatu.shoe_service.enums.FilterOperator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterCriteria {

    @NotBlank(message = MessageConstants.VALIDATION_REQUIRED)
    private String field;

    @NotNull(message = MessageConstants.VALIDATION_REQUIRED)
    private FilterOperator operator;

    @NotNull(message = MessageConstants.VALIDATION_REQUIRED)
    private Object value;
}
