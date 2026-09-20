package com.capatu.reminder_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DefaultModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "creation_time", updatable = false, nullable = false)
    private LocalDateTime creationTime;

    @LastModifiedDate
    @Column(name = "modification_time", nullable = false)
    private LocalDateTime modificationTime;

    @Column(name = "created_by", nullable = false)
    private String createdBy = "system";

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy = "system";
}
