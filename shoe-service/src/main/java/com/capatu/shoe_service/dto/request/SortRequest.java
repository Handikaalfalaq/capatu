package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.MessageConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortRequest {

    @NotBlank(message = MessageConstants.VALIDATION_REQUIRED)
    private String field;

    @Pattern(regexp = MessageConstants.SORT_DIRECTION_FORMAT, message = MessageConstants.VALIDATION_SORT_DIRECTION)
    private String direction;
}
