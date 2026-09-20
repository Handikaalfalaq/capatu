package com.capatu.reminder_service.rule;

import java.util.List;

public interface ReminderRule {

    List<ReminderCandidate> evaluate(RuleContext context);
}
