package com.capatu.shoe_service.repository;

import com.capatu.shoe_service.entity.ShoeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShoeRepository extends JpaRepository<ShoeModel, Long>, JpaSpecificationExecutor<ShoeModel> {

    /** Pencarian berhalaman; jenis sepatu ikut diambil dalam query yang sama (JOIN) supaya tidak N+1. */
    @Override
    @EntityGraph(attributePaths = "shoeTypeRef")
    Page<ShoeModel> findAll(Specification<ShoeModel> specification, Pageable pageable);
}
