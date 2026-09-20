package com.capatu.shoe_service.service.impl;

import com.capatu.shoe_service.constant.Constants;
import com.capatu.shoe_service.dto.request.ShoeRequest;
import com.capatu.shoe_service.dto.request.ShoeSearchRequest;
import com.capatu.shoe_service.dto.response.PageResponse;
import com.capatu.shoe_service.entity.RefCodeModel;
import com.capatu.shoe_service.entity.ShoeModel;
import com.capatu.shoe_service.repository.RefCodeRepository;
import com.capatu.shoe_service.repository.ShoeRepository;
import com.capatu.shoe_service.service.ShoeService;
import com.capatu.shoe_service.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoeServiceImpl implements ShoeService {

    private final ShoeRepository shoeRepository;
    private final RefCodeRepository refCodeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ShoeModel> findAll(){
        return shoeRepository.findAll();
    }

    @Override
    public PageResponse<ShoeModel> getPageableByFilters(ShoeSearchRequest request) {
        Page<ShoeModel> result = shoeRepository.findAll(toSpecification(request), request.toPageable());

        return PageResponse.from(result);
    }

    @Override
    @Transactional(readOnly = true)
    public ShoeModel findById(Long id){
        return shoeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, Constants.NOT_FOUND.formatted(Constants.RESOURCE_REF_CODE)));
    }

    @Override
    @Transactional
    public void create(ShoeRequest request){
        RefCodeModel shoeType = validateShoeType(request.getShoeTypeRefId());
        Set<RefCodeModel> features = validateFeatures(request.getFeatureRefIds());

        shoeRepository.save(shoeModel(new ShoeModel(), request, shoeType, features));
    }

    @Override
    @Transactional
    public void update(Long id, ShoeRequest request){
        ShoeModel shoeExisting = findById(id);

        RefCodeModel shoeType = validateShoeType(request.getShoeTypeRefId());
        Set<RefCodeModel> features = validateFeatures(request.getFeatureRefIds());

        shoeRepository.save(shoeModel(shoeExisting, request, shoeType, features));
    }


    @Override
    public void delete(Long id){
        ShoeModel shoeModel = findById(id);

        try {
            shoeRepository.delete(shoeModel);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    Constants.IN_USE.formatted(Constants.RESOURCE_REF_CODE));
        }
    }

    private RefCodeModel validateShoeType(Long shoeTypeRefId){
        return refCodeRepository.findByIdInAndType(List.of(shoeTypeRefId), Constants.SHOE_TYPE).stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, Constants.INVALID_SHOE_TYPE_REF));
    }

    private Set<RefCodeModel> validateFeatures(Set<Long> featureRefIds){
        if (featureRefIds == null) {
            return null;
        }

        Set<RefCodeModel> features = featureRefIds.isEmpty()
                ? new HashSet<>()
                : new HashSet<>(refCodeRepository.findByIdInAndType(featureRefIds, Constants.SHOE_FEATURE));

        Set<Long> validIds = features.stream()
                .map(RefCodeModel::getId)
                .collect(Collectors.toSet());

        List<Long> invalidIds = featureRefIds.stream()
                .filter(id -> id == null || !validIds.contains(id))
                .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
                .toList();

        if (!invalidIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_FEATURE_REF.formatted(invalidIds));
        }
        return features;
    }

    private ShoeModel shoeModel(ShoeModel shoeModel, ShoeRequest request, RefCodeModel shoeType, Set<RefCodeModel> features){
        shoeModel.setName(request.getName());
        shoeModel.setBrand(request.getBrand());
        shoeModel.setModel(request.getModel());
        shoeModel.setShoeTypeRef(shoeType);
        shoeModel.setColor(request.getColor());
        shoeModel.setSizeValue(request.getSizeValue());
        shoeModel.setSizeSystem(request.getSizeSystem());
        shoeModel.setWeightGrams(request.getWeightGrams());
        shoeModel.setPurchaseDate(request.getPurchaseDate());
        shoeModel.setPurchasePrice(request.getPurchasePrice());
        shoeModel.setTargetLifespanKm(request.getTargetLifespanKm());

        if (features != null) {
            shoeModel.getFeatures().clear();
            shoeModel.getFeatures().addAll(features);
        }

        return shoeModel;
    }


    private Specification<ShoeModel> toSpecification(ShoeSearchRequest request) {
        return Specification.allOf(
                SpecificationUtils.contains("name", request.getName()),
                SpecificationUtils.contains("brand", request.getBrand()),
                SpecificationUtils.equalId("shoeTypeRef", request.getShoeTypeRefId()),
                SpecificationUtils.equal("retired", request.getRetired()));
    }
}
