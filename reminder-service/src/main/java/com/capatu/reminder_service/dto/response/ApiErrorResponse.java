package com.capatu.reminder_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(Instant timestamp, int status, String error, String message, String path) {
}
