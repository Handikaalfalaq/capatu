package com.capatu.shoe_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "usage_log")
public class UsageLogModel extends DefaultModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shoe_id", nullable = false)
    private ShoeModel shoe;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @Column(name = "distance_km", nullable = false)
    private BigDecimal distanceKm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_type_ref_id", nullable = false)
    private RefCodeModel activityTypeRef;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "notes")
    private String notes;
}
