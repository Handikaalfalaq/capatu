package com.capatu.shoe_service.service.impl;

import com.capatu.shoe_service.constant.Constants;
import com.capatu.shoe_service.dto.request.MaintenanceLogRequest;
import com.capatu.shoe_service.dto.request.MaintenanceLogSearchRequest;
import com.capatu.shoe_service.dto.response.PageResponse;
import com.capatu.shoe_service.dto.response.UsageLogSummary;
import com.capatu.shoe_service.entity.MaintenanceLogModel;
import com.capatu.shoe_service.entity.RefCodeModel;
import com.capatu.shoe_service.entity.ShoeModel;
import com.capatu.shoe_service.repository.MaintenanceLogRepository;
import com.capatu.shoe_service.repository.RefCodeRepository;
import com.capatu.shoe_service.repository.ShoeRepository;
import com.capatu.shoe_service.repository.UsageLogRepository;
import com.capatu.shoe_service.service.MaintenanceLogService;
import com.capatu.shoe_service.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceLogServiceImpl implements MaintenanceLogService {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final UsageLogRepository usageLogRepository;
    private final ShoeRepository shoeRepository;
    private final RefCodeRepository refCodeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceLogModel> findAll() {
        return maintenanceLogRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MaintenanceLogModel> getPageableByFilters(MaintenanceLogSearchRequest request) {
        Page<MaintenanceLogModel> result = maintenanceLogRepository.findAll(toSpecification(request), request.toPageable());

        return PageResponse.from(result);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceLogModel findById(Long id) {
        return maintenanceLogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, Constants.NOT_FOUND.formatted(Constants.RESOURCE_MAINTENANCE_LOG)));
    }

    @Override
    @Transactional
    public void create(MaintenanceLogRequest request) {
        ShoeModel shoeModel = findShoe(request.getShoeId());
        validateShoeActive(shoeModel);
        RefCodeModel refCodeModel = validateActionRef(request.getActionRefId());
        validateActionDate(request.getActionDate(), shoeModel);

        maintenanceLogRepository.saveAndFlush(maintenanceLogModel(new MaintenanceLogModel(), request, shoeModel, refCodeModel));
        recalculate(shoeModel);
    }

    @Override
    @Transactional
    public void update(Long id, MaintenanceLogRequest request) {
        MaintenanceLogModel maintenanceLogExisting = findById(id);
        validateShoeUnchanged(maintenanceLogExisting, request.getShoeId());

        ShoeModel shoeModel = findShoe(maintenanceLogExisting.getShoe().getId());
        RefCodeModel refCodeModel = validateActionRef(request.getActionRefId());
        validateActionDate(request.getActionDate(), shoeModel);

        maintenanceLogRepository.saveAndFlush(maintenanceLogModel(maintenanceLogExisting, request, shoeModel, refCodeModel));
        recalculate(shoeModel);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MaintenanceLogModel maintenanceLogExisting = findById(id);
        ShoeModel shoeModel = findShoe(maintenanceLogExisting.getShoe().getId());

        maintenanceLogRepository.delete(maintenanceLogExisting);
        maintenanceLogRepository.flush();
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
                    Constants.SHOE_RETIRED_MAINTENANCE.formatted(Constants.RESOURCE_SHOE));
        }
    }

    private void validateShoeUnchanged(MaintenanceLogModel maintenanceLogExisting, Long requestedShoeId) {
        if (!maintenanceLogExisting.getShoe().getId().equals(requestedShoeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Constants.SHOE_CANNOT_CHANGE.formatted(Constants.RESOURCE_MAINTENANCE_LOG));
        }
    }

    private RefCodeModel validateActionRef(Long actionRefId) {
        return refCodeRepository.findByIdInAndType(List.of(actionRefId), Constants.MAINTENANCE_ACTION).stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, Constants.INVALID_MAINTENANCE_ACTION_REF));
    }

    private void validateActionDate(LocalDate actionDate, ShoeModel shoeModel) {
        if (actionDate.isBefore(shoeModel.getPurchaseDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Constants.ACTION_BEFORE_PURCHASE.formatted(shoeModel.getPurchaseDate()));
        }
    }

    private void recalculate(ShoeModel shoeModel) {
        shoeModel.setLastWashedAt(maintenanceLogRepository.findLastActionDate(shoeModel.getId(), Constants.WASH_CODE));
        shoeRepository.saveAndFlush(shoeModel);

        UsageLogSummary summary = usageLogRepository.summarize(shoeModel.getId());
        shoeModel.setUsageCountSinceLastWash(summary.getUsageCountSinceLastWash().intValue());

        shoeRepository.save(shoeModel);
    }

    private MaintenanceLogModel maintenanceLogModel(MaintenanceLogModel maintenanceLogModel, MaintenanceLogRequest request, ShoeModel shoeModel, RefCodeModel refCodeModel) {
        maintenanceLogModel.setShoe(shoeModel);
        maintenanceLogModel.setActionRef(refCodeModel);
        maintenanceLogModel.setActionDate(request.getActionDate());
        maintenanceLogModel.setNotes(request.getNotes());

        return maintenanceLogModel;
    }

    private Specification<MaintenanceLogModel> toSpecification(MaintenanceLogSearchRequest request) {
        return Specification.allOf(
                SpecificationUtils.equalId("shoe", request.getShoeId()),
                SpecificationUtils.equalId("actionRef", request.getActionRefId()),
                SpecificationUtils.between("actionDate", request.getActionDateFrom(), request.getActionDateTo(),
                        LocalDate.EPOCH, LocalDate.now()));
    }
}
