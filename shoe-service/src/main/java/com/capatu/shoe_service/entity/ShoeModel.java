package com.capatu.shoe_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "shoe")
public class ShoeModel extends DefaultModel {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shoe_type_ref_id")
    private RefCodeModel shoeTypeRef;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "size_value", nullable = false)
    private BigDecimal sizeValue;

    @Column(name = "size_system", nullable = false)
    private String sizeSystem;

    @Column(name = "weight_grams")
    private Integer weightGrams;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;

    @Column(name = "target_lifespan_km")
    private Integer targetLifespanKm;

    @Column(name = "retired", nullable = false)
    private Boolean retired = false;

    @Column(name = "retired_at")
    private LocalDate retiredAt;

    @Column(name = "total_distance_km", nullable = false)
    private BigDecimal totalDistanceKm = BigDecimal.ZERO;

    @Column(name = "usage_count", nullable = false)
    private Integer usageCount = 0;

    @Column(name = "usage_count_since_last_wash", nullable = false)
    private Integer usageCountSinceLastWash = 0;

    @Column(name = "last_used_at")
    private LocalDate lastUsedAt;

    @Column(name = "last_washed_at")
    private LocalDate lastWashedAt;

    @ManyToMany
    @BatchSize(size = 50)
    @JoinTable(name = "shoe_feature_map",
            joinColumns = @JoinColumn(name = "shoe_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_ref_id"))
    private Set<RefCodeModel> features = new HashSet<>();
}
