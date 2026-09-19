package com.capatu.shoe_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ref_code")

public class RefCodeModel extends DefaultModel {
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "code_name", nullable = false)
    private String codeName;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
