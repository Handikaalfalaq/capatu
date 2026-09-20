package com.capatu.shoe_service.controller;

import com.capatu.shoe_service.constant.Constants;
import com.capatu.shoe_service.dto.request.MaintenanceLogRequest;
import com.capatu.shoe_service.dto.request.MaintenanceLogSearchRequest;
import com.capatu.shoe_service.dto.response.ApiResponse;
import com.capatu.shoe_service.service.MaintenanceLogService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/maintenance-log")
public class MaintenanceLogController {

    private final MaintenanceLogService maintenanceLogService;

    public MaintenanceLogController(MaintenanceLogService maintenanceLogService) {
        this.maintenanceLogService = maintenanceLogService;
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(maintenanceLogService.findAll());
    }

    @PostMapping("/pageable")
    public ResponseEntity<?> getPageableByFilters(@Valid @RequestBody MaintenanceLogSearchRequest request) {
        return ResponseEntity.ok(maintenanceLogService.getPageableByFilters(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceLogService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody MaintenanceLogRequest request) {
        maintenanceLogService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        Constants.CREATED.formatted(Constants.RESOURCE_MAINTENANCE_LOG)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody MaintenanceLogRequest request) {
        maintenanceLogService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        Constants.UPDATED.formatted(Constants.RESOURCE_MAINTENANCE_LOG)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        maintenanceLogService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        Constants.DELETED.formatted(Constants.RESOURCE_MAINTENANCE_LOG)));
    }
}
