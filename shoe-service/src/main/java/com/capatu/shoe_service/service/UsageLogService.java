package com.capatu.shoe_service.service;

import com.capatu.shoe_service.dto.request.UsageLogRequest;
import com.capatu.shoe_service.dto.request.UsageLogSearchRequest;
import com.capatu.shoe_service.dto.response.PageResponse;
import com.capatu.shoe_service.entity.UsageLogModel;

import java.util.List;

public interface UsageLogService {

    List<UsageLogModel> findAll();

    PageResponse<UsageLogModel> getPageableByFilters(UsageLogSearchRequest request);

    UsageLogModel findById(Long id);

    void create(UsageLogRequest request);

    void update(Long id, UsageLogRequest request);

    void delete(Long id);
}
