package com.capatu.shoe_service.service;

import com.capatu.shoe_service.dto.request.RefCodeRequest;
import com.capatu.shoe_service.dto.request.RefCodeSearchRequest;
import com.capatu.shoe_service.dto.response.PageResponse;
import com.capatu.shoe_service.dto.response.RefCodeResponse;
import com.capatu.shoe_service.entity.RefCodeModel;

import java.util.List;

public interface RefCodeService {

    List<RefCodeResponse> findAllCode();

    List<RefCodeModel> allRefCodeByType(String type);

    PageResponse<RefCodeModel> getPageableByFilters(RefCodeSearchRequest request);

    RefCodeModel findById(Long id);

    void create(RefCodeRequest request);

    void update(Long id, RefCodeRequest request);

    void delete(Long id);
}
