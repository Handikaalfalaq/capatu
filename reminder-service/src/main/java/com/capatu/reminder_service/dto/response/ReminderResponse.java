package com.capatu.reminder_service.dto.response;

import com.capatu.reminder_service.entity.ReminderModel;
import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderStatus;
import com.capatu.reminder_service.enums.ReminderType;

import java.time.LocalDateTime;

public record ReminderResponse(Long id, Long shoeId, String shoeName, ReminderType type, ReminderSeverity severity,
                               String message, ReminderStatus status, LocalDateTime detectedAt,
                               LocalDateTime resolvedAt) {

    public static ReminderResponse from(ReminderModel reminder) {
        return new ReminderResponse(reminder.getId(), reminder.getShoeId(), reminder.getShoeName(),
                reminder.getType(), reminder.getSeverity(), reminder.getMessage(), reminder.getStatus(),
                reminder.getDetectedAt(), reminder.getResolvedAt());
    }
}
