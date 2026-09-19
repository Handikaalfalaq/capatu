package com.capatu.shoe_service.repository;

import com.capatu.shoe_service.dto.response.RefCodeResponse;
import com.capatu.shoe_service.entity.RefCodeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefCodeRepository extends JpaRepository<RefCodeModel, Long>, JpaSpecificationExecutor<RefCodeModel> {

    @Query("SELECT DISTINCT r.type AS type, r.typeName AS typeName FROM RefCodeModel r WHERE r.isActive = true ORDER BY r.type ASC")
    List<RefCodeResponse> findAllCode();

    @Query("SELECT r FROM RefCodeModel r WHERE r.type = :type AND r.isActive = true ORDER BY r.id ASC")
    List<RefCodeModel> allRefCodeByType(@Param("type") String type);

    Optional<RefCodeModel> findByTypeAndCode(String type, String code);

}
