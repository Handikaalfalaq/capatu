package com.capatu.reminder_service.rule;

import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderType;

public record ReminderCandidate(Long shoeId, String shoeName, ReminderType type,
                                ReminderSeverity severity, String message) {
}
