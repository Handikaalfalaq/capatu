package com.capatu.reminder_service.rule;

import com.capatu.reminder_service.client.dto.ShoeSnapshot;
import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WashRule implements ReminderRule {

    private final int washThreshold;

    public WashRule(@Value("${capatu.reminder.wash-threshold}") int washThreshold) {
        this.washThreshold = washThreshold;
    }

    @Override
    public List<ReminderCandidate> evaluate(RuleContext context) {
        return context.shoes().stream()
                .filter(ShoeSnapshot::isActive)
                .filter(shoe -> shoe.usageCountSinceLastWash() != null
                        && shoe.usageCountSinceLastWash() >= washThreshold)
                .map(shoe -> new ReminderCandidate(shoe.id(), shoe.name(), ReminderType.WASH, ReminderSeverity.INFO,
                        "%s sudah dipakai %d kali sejak dicuci terakhir, waktunya dicuci"
                                .formatted(shoe.name(), shoe.usageCountSinceLastWash())))
                .toList();
    }
}
