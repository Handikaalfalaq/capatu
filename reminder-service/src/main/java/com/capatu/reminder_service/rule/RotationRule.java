package com.capatu.reminder_service.rule;

import com.capatu.reminder_service.client.dto.RotationSnapshot;
import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RotationRule implements ReminderRule {

    @Override
    public List<ReminderCandidate> evaluate(RuleContext context) {
        RotationSnapshot rotation = context.rotation();
        if (rotation == null || rotation.shares() == null) {
            return List.of();
        }
        return rotation.shares().stream()
                .filter(RotationSnapshot.Share::unhealthy)
                .map(share -> new ReminderCandidate(share.shoeId(), share.shoeName(), ReminderType.ROTATION,
                        ReminderSeverity.WARNING,
                        "%s menyumbang %s%% pemakaian dalam %d hari terakhir, sepatu lain jarang dipakai"
                                .formatted(share.shoeName(), share.percent().stripTrailingZeros().toPlainString(),
                                        rotation.days())))
                .toList();
    }
}
