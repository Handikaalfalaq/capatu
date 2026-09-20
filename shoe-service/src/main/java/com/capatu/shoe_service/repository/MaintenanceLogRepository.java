package com.capatu.shoe_service.repository;

import com.capatu.shoe_service.entity.MaintenanceLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLogModel, Long>, JpaSpecificationExecutor<MaintenanceLogModel> {

    @Query("SELECT MAX(m.actionDate) FROM MaintenanceLogModel m WHERE m.shoe.id = :shoeId AND m.actionRef.code = :code")
    LocalDate findLastActionDate(@Param("shoeId") Long shoeId, @Param("code") String code);
}
