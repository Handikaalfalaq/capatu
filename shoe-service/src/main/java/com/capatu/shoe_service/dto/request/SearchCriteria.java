package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.MessageConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchCriteria {

    @Valid
    private List<FilterCriteria> filters;

    @Valid
    private List<SortRequest> sorts;

    @Min(value = 0, message = MessageConstants.VALIDATION_MIN_VALUE)
    private Integer page;

    @Min(value = 1, message = MessageConstants.VALIDATION_MIN_VALUE)
    @Max(value = 100, message = MessageConstants.VALIDATION_MAX_VALUE)
    private Integer size;
}
