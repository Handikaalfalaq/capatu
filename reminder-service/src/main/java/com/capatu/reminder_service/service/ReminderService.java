package com.capatu.reminder_service.service;

import com.capatu.reminder_service.dto.response.EvaluationResult;
import com.capatu.reminder_service.dto.response.ReminderResponse;
import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderStatus;

import java.util.List;

public interface ReminderService {

    List<ReminderResponse> findAll(ReminderStatus status, ReminderSeverity severity);

    ReminderResponse findById(Long id);

    void acknowledge(Long id);

    EvaluationResult evaluate();
}
