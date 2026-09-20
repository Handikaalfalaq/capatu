package com.capatu.reminder_service.rule;

import com.capatu.reminder_service.client.dto.RotationSnapshot;
import com.capatu.reminder_service.client.dto.ShoeSnapshot;

import java.util.List;

public record RuleContext(List<ShoeSnapshot> shoes, RotationSnapshot rotation) {
}
