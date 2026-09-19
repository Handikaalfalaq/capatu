package com.capatu.shoe_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(Instant timestamp, int status, String error, String message,
                               String path, List<FieldError> errors) {

    public record FieldError(String field, String message) {
    }
}
