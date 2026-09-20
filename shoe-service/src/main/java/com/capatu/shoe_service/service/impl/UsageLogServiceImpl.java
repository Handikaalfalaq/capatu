package com.capatu.shoe_service.service.impl;

import com.capatu.shoe_service.constant.Constants;
import com.capatu.shoe_service.dto.request.UsageLogRequest;
import com.capatu.shoe_service.dto.request.UsageLogSearchRequest;
import com.capatu.shoe_service.dto.response.PageResponse;
import com.capatu.shoe_service.dto.response.UsageLogSummary;
import com.capatu.shoe_service.entity.RefCodeModel;
import com.capatu.shoe_service.entity.ShoeModel;
import com.capatu.shoe_service.entity.UsageLogModel;
import com.capatu.shoe_service.repository.RefCodeRepository;
import com.capatu.shoe_service.repository.ShoeRepository;
import com.capatu.shoe_service.repository.UsageLogRepository;
import com.capatu.shoe_service.service.UsageLogService;
import com.capatu.shoe_service.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsageLogServiceImpl implements UsageLogService {

    private final UsageLogRepository usageLogRepository;
    private final ShoeRepository shoeRepository;
    private final RefCodeRepository refCodeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UsageLogModel> findAll() {
        return usageLogRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UsageLogModel> getPageableByFilters(UsageLogSearchRequest request) {
        Page<UsageLogModel> result = usageLogRepository.findAll(toSpecification(request), request.toPageable());

        return PageResponse.from(result);
    }

    @Override
    @Transactional(readOnly = true)
    public UsageLogModel findById(Long id) {
        return usageLogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, Constants.NOT_FOUND.formatted(Constants.RESOURCE_USAGE_LOG)));
    }

    @Override
    @Transactional
    public void create(UsageLogRequest request) {
        ShoeModel shoeModel = findShoe(request.getShoeId());
        validateShoeActive(shoeModel);
        RefCodeModel refCodeModel = validateActivityTypeRef(request.getActivityTypeRefId());
        validateActivityDate(request.getActivityDate(), shoeModel);

        usageLogRepository.saveAndFlush(usageLogModel(new UsageLogModel(), request, shoeModel, refCodeModel));
        recalculate(shoeModel);
    }

    @Override
    @Transactional
    public void update(Long id, UsageLogRequest request) {
        UsageLogModel usageLogExisting = findById(id);
        validateShoeUnchanged(usageLogExisting, request.getShoeId());

        ShoeModel shoeModel = findShoe(usageLogExisting.getShoe().getId());
        RefCodeModel refCodeModel = validateActivityTypeRef(request.getActivityTypeRefId());
        validateActivityDate(request.getActivityDate(), shoeModel);

        usageLogRepository.saveAndFlush(usageLogModel(usageLogExisting, request, shoeModel, refCodeModel));
        recalculate(shoeModel);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UsageLogModel usageLogExisting = findById(id);
        ShoeModel shoeModel = findShoe(usageLogExisting.getShoe().getId());

        usageLogRepository.delete(usageLogExisting);
        usageLogRepository.flush();
        recalculate(shoeModel);
    }

    private ShoeModel findShoe(Long shoeId) {
        return shoeRepository.findById(shoeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, Constants.NOT_FOUND.formatted(Constants.RESOURCE_SHOE)));
    }

    private void validateShoeActive(ShoeModel shoeModel) {
        if (Boolean.TRUE.equals(shoeModel.getRetired())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    Constants.SHOE_RETIRED.formatted(Constants.RESOURCE_SHOE));
        }
    }

    private void validateShoeUnchanged(UsageLogModel usageLogExisting, Long requestedShoeId) {
        if (!usageLogExisting.getShoe().getId().equals(requestedShoeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Constants.SHOE_CANNOT_CHANGE.formatted(Constants.RESOURCE_USAGE_LOG));
        }
    }

    private RefCodeModel validateActivityTypeRef(Long activityTypeRefId) {
        return refCodeRepository.findByIdInAndType(List.of(activityTypeRefId), Constants.ACTIVITY_TYPE).stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, Constants.INVALID_ACTIVITY_TYPE_REF));
    }

    private void validateActivityDate(LocalDate activityDate, ShoeModel shoeModel) {
        if (activityDate.isBefore(shoeModel.getPurchaseDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Constants.USAGE_BEFORE_PURCHASE.formatted(shoeModel.getPurchaseDate()));
        }
    }

    private void recalculate(ShoeModel shoeModel) {
        UsageLogSummary summary = usageLogRepository.summarize(shoeModel.getId());

        shoeModel.setTotalDistanceKm(summary.getTotalDistanceKm());
        shoeModel.setUsageCount(summary.getUsageCount().intValue());
        shoeModel.setLastUsedAt(summary.getLastUsedAt());
        shoeModel.setUsageCountSinceLastWash(summary.getUsageCountSinceLastWash().intValue());

        shoeRepository.save(shoeModel);
    }

    private UsageLogModel usageLogModel(UsageLogModel usageLogModel, UsageLogRequest request, ShoeModel shoeModel, RefCodeModel refCodeModel) {
        usageLogModel.setShoe(shoeModel);
        usageLogModel.setActivityDate(request.getActivityDate());
        usageLogModel.setDistanceKm(request.getDistanceKm());
        usageLogModel.setActivityTypeRef(refCodeModel);
        usageLogModel.setDurationMinutes(request.getDurationMinutes());
        usageLogModel.setNotes(request.getNotes());

        return usageLogModel;
    }

    private Specification<UsageLogModel> toSpecification(UsageLogSearchRequest request) {
        return Specification.allOf(
                SpecificationUtils.equalId("shoe", request.getShoeId()),
                SpecificationUtils.equalId("activityTypeRef", request.getActivityTypeRefId()),
                SpecificationUtils.between("activityDate", request.getActivityDateFrom(), request.getActivityDateTo(),
                        LocalDate.EPOCH, LocalDate.now()),
                SpecificationUtils.between("distanceKm", request.getDistanceKmFrom(), request.getDistanceKmTo(),
                        BigDecimal.ZERO, Constants.MAX_DISTANCE_KM),
                SpecificationUtils.between("durationMinutes", request.getDurationMinutesFrom(), request.getDurationMinutesTo(),
                        0, Integer.MAX_VALUE));
    }
}
