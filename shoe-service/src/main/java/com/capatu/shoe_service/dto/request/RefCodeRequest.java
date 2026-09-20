package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefCodeRequest {

    @NotBlank(message = Constants.VALIDATION_REQUIRED)
    @Size(max = 50, message = Constants.VALIDATION_MAX_LENGTH)
    @Pattern(regexp = Constants.NAME_FORMAT, message = Constants.VALIDATION_NAME_FORMAT)
    private String typeName;

    @NotBlank(message = Constants.VALIDATION_REQUIRED)
    @Size(max = 50, message = Constants.VALIDATION_MAX_LENGTH)
    @Pattern(regexp = Constants.NAME_FORMAT, message = Constants.VALIDATION_NAME_FORMAT)
    private String codeName;

    private String description;
}
