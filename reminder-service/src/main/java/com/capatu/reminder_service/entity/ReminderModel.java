package com.capatu.reminder_service.entity;

import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderStatus;
import com.capatu.reminder_service.enums.ReminderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reminder")
public class ReminderModel extends DefaultModel {

    @Column(name = "shoe_id", nullable = false)
    private Long shoeId;

    @Column(name = "shoe_name", nullable = false)
    private String shoeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ReminderType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private ReminderSeverity severity;

    @Column(name = "message", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReminderStatus status;

    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
