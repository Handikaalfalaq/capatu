package com.capatu.shoe_service.dto.request;

import com.capatu.shoe_service.constant.MessageConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class PageableRequest {

    @Min(value = 0, message = MessageConstants.VALIDATION_MIN_VALUE)
    private Integer page;

    @Min(value = 1, message = MessageConstants.VALIDATION_MIN_VALUE)
    @Max(value = 100, message = MessageConstants.VALIDATION_MAX_VALUE)
    private Integer size;

    public Pageable toPageable() {
        return PageRequest.of(
                page != null ? page : MessageConstants.DEFAULT_PAGE,
                size != null ? size : MessageConstants.DEFAULT_SIZE,
                Sort.by(MessageConstants.DEFAULT_SORT_FIELD));
    }
}
