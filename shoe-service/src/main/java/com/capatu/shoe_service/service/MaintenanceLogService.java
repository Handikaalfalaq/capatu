package com.capatu.shoe_service.service;

import com.capatu.shoe_service.dto.request.MaintenanceLogRequest;
import com.capatu.shoe_service.dto.request.MaintenanceLogSearchRequest;
import com.capatu.shoe_service.dto.response.PageResponse;
import com.capatu.shoe_service.entity.MaintenanceLogModel;

import java.util.List;

public interface MaintenanceLogService {

    List<MaintenanceLogModel> findAll();

    PageResponse<MaintenanceLogModel> getPageableByFilters(MaintenanceLogSearchRequest request);

    MaintenanceLogModel findById(Long id);

    void create(MaintenanceLogRequest request);

    void update(Long id, MaintenanceLogRequest request);

    void delete(Long id);
}
