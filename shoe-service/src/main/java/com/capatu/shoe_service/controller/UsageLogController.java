package com.capatu.shoe_service.controller;

import com.capatu.shoe_service.constant.Constants;
import com.capatu.shoe_service.dto.request.UsageLogRequest;
import com.capatu.shoe_service.dto.request.UsageLogSearchRequest;
import com.capatu.shoe_service.dto.response.ApiResponse;
import com.capatu.shoe_service.service.UsageLogService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/usage-log")
public class UsageLogController {

    private final UsageLogService usageLogService;

    public UsageLogController(UsageLogService usageLogService) {
        this.usageLogService = usageLogService;
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(usageLogService.findAll());
    }

    @PostMapping("/pageable")
    public ResponseEntity<?> getPageableByFilters(@Valid @RequestBody UsageLogSearchRequest request) {
        return ResponseEntity.ok(usageLogService.getPageableByFilters(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usageLogService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody UsageLogRequest request) {
        usageLogService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        Constants.CREATED.formatted(Constants.RESOURCE_USAGE_LOG)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UsageLogRequest request) {
        usageLogService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        Constants.UPDATED.formatted(Constants.RESOURCE_USAGE_LOG)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        usageLogService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        Constants.DELETED.formatted(Constants.RESOURCE_USAGE_LOG)));
    }
}
