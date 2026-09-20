package com.capatu.reminder_service.rule;

import com.capatu.reminder_service.client.dto.ShoeSnapshot;
import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Component
public class ReplaceRule implements ReminderRule {

    private final BigDecimal warningRatio;

    public ReplaceRule(@Value("${capatu.reminder.replace-warning-ratio}") BigDecimal warningRatio) {
        this.warningRatio = warningRatio;
    }

    @Override
    public List<ReminderCandidate> evaluate(RuleContext context) {
        return context.shoes().stream()
                .filter(ShoeSnapshot::isActive)
                .filter(shoe -> shoe.targetLifespanKm() != null && shoe.targetLifespanKm() > 0
                        && shoe.totalDistanceKm() != null)
                .map(this::toCandidate)
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<ReminderCandidate> toCandidate(ShoeSnapshot shoe) {
        BigDecimal target = BigDecimal.valueOf(shoe.targetLifespanKm());
        BigDecimal total = shoe.totalDistanceKm();

        ReminderSeverity severity;
        if (total.compareTo(target) >= 0) {
            severity = ReminderSeverity.CRITICAL;
        } else if (total.compareTo(target.multiply(warningRatio)) >= 0) {
            severity = ReminderSeverity.WARNING;
        } else {
            return Optional.empty();
        }

        BigDecimal percent = total.multiply(BigDecimal.valueOf(100)).divide(target, 1, RoundingMode.HALF_UP);
        String message = "%s sudah menempuh %s km dari target %d km (%s%%), waktunya diganti"
                .formatted(shoe.name(), total.stripTrailingZeros().toPlainString(), shoe.targetLifespanKm(), percent);
        return Optional.of(new ReminderCandidate(shoe.id(), shoe.name(), ReminderType.REPLACE, severity, message));
    }
}
