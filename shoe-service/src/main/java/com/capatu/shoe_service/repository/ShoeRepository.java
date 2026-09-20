package com.capatu.shoe_service.repository;

import com.capatu.shoe_service.entity.ShoeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShoeRepository extends JpaRepository<ShoeModel, Long>, JpaSpecificationExecutor<ShoeModel> {
}
