package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.MessageConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefCodeRequest {

    @NotBlank(message = MessageConstants.VALIDATION_REQUIRED)
    @Size(max = 50, message = MessageConstants.VALIDATION_MAX_LENGTH)
    @Pattern(regexp = MessageConstants.NAME_FORMAT, message = MessageConstants.VALIDATION_NAME_FORMAT)
    private String typeName;

    @NotBlank(message = MessageConstants.VALIDATION_REQUIRED)
    @Size(max = 50, message = MessageConstants.VALIDATION_MAX_LENGTH)
    @Pattern(regexp = MessageConstants.NAME_FORMAT, message = MessageConstants.VALIDATION_NAME_FORMAT)
    private String codeName;

    private String description;

    private Boolean isActive;
}
