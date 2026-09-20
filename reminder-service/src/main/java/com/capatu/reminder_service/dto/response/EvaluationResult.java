package com.capatu.reminder_service.dto.response;

public record EvaluationResult(int created, int updated, int reactivated, int resolved, int open) {
}
