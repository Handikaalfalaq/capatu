package com.capatu.shoe_service.service;

import com.capatu.shoe_service.dto.request.ShoeRequest;
import com.capatu.shoe_service.dto.request.ShoeSearchRequest;
import com.capatu.shoe_service.dto.response.PageResponse;
import com.capatu.shoe_service.entity.ShoeModel;

import java.util.List;

public interface ShoeService {

    List<ShoeModel> findAll();

    PageResponse<ShoeModel> getPageableByFilters(ShoeSearchRequest request);

    ShoeModel findById(Long id);

    void create(ShoeRequest request);

    void update(Long id, ShoeRequest request);

    void delete(Long id);
}
