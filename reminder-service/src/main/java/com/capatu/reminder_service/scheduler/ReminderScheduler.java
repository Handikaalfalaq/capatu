package com.capatu.reminder_service.scheduler;

import com.capatu.reminder_service.dto.response.EvaluationResult;
import com.capatu.reminder_service.exception.ShoeServiceUnavailableException;
import com.capatu.reminder_service.service.ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "capatu.reminder.scheduler-enabled", havingValue = "true", matchIfMissing = true)
public class ReminderScheduler {

    private final ReminderService reminderService;

    @Scheduled(initialDelayString = "${capatu.reminder.evaluation-initial-delay-ms}",
            fixedDelayString = "${capatu.reminder.evaluation-interval-ms}")
    public void evaluate() {
        try {
            EvaluationResult result = reminderService.evaluate();
            log.info("Evaluasi reminder selesai: {}", result);
        } catch (ShoeServiceUnavailableException e) {
            log.warn("Evaluasi reminder dilewati, shoe-service tidak dapat dihubungi");
        }
    }
}
