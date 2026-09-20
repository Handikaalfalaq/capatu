package com.capatu.shoe_service.repository;

import com.capatu.shoe_service.dto.response.RefCodeResponse;
import com.capatu.shoe_service.entity.RefCodeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RefCodeRepository extends JpaRepository<RefCodeModel, Long>, JpaSpecificationExecutor<RefCodeModel> {

    @Query("SELECT DISTINCT r.type AS type, r.typeName AS typeName FROM RefCodeModel r ORDER BY r.type ASC")
    List<RefCodeResponse> findAllCode();

    @Query("SELECT r FROM RefCodeModel r WHERE r.type = :type ORDER BY r.id ASC")
    List<RefCodeModel> allRefCodeByType(@Param("type") String type);

    Optional<RefCodeModel> findByTypeAndCode(String type, String code);

    List<RefCodeModel> findByIdInAndType(Collection<Long> ids, String type);

    @Query(value = """
            SELECT EXISTS (SELECT 1 FROM shoe             WHERE shoe_type_ref_id     = :id)
                OR EXISTS (SELECT 1 FROM shoe_feature_map WHERE feature_ref_id       = :id)
                OR EXISTS (SELECT 1 FROM usage_log        WHERE activity_type_ref_id = :id)
                OR EXISTS (SELECT 1 FROM maintenance_log  WHERE action_ref_id        = :id)
            """, nativeQuery = true)
    boolean isInUse(@Param("id") Long id);

}
