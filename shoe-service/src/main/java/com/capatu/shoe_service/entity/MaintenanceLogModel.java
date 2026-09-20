package com.capatu.shoe_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "maintenance_log")
public class MaintenanceLogModel extends DefaultModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shoe_id", nullable = false)
    private ShoeModel shoe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_ref_id", nullable = false)
    private RefCodeModel actionRef;

    @Column(name = "action_date", nullable = false)
    private LocalDate actionDate;

    @Column(name = "notes")
    private String notes;
}
