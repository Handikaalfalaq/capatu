package com.capatu.shoe_service.controller;


import com.capatu.shoe_service.constant.MessageConstants;
import com.capatu.shoe_service.dto.request.RefCodeRequest;
import com.capatu.shoe_service.dto.request.SearchCriteria;
import com.capatu.shoe_service.dto.response.ApiResponse;
import com.capatu.shoe_service.service.RefCodeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/ref-code")
public class RefCodeController {

    private final RefCodeService refCodeService;

    public RefCodeController(RefCodeService refCodeService) {
        this.refCodeService = refCodeService;
    }

    @GetMapping()
    public ResponseEntity<?> findAllCode() {
        return ResponseEntity.ok(refCodeService.findAllCode());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getByType(@PathVariable String type) {
        return ResponseEntity.ok(refCodeService.allRefCodeByType(type));
    }

    @PostMapping("/pageable")
    public ResponseEntity<?> getPageableByFilters(@Valid @RequestBody SearchCriteria searchCriteria) {
        return ResponseEntity.ok(refCodeService.getPageableByFilters(searchCriteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(refCodeService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody RefCodeRequest request) {
        refCodeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        MessageConstants.CREATED.formatted(MessageConstants.RESOURCE_REF_CODE)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody RefCodeRequest request) {
        refCodeService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        MessageConstants.UPDATED.formatted(MessageConstants.RESOURCE_REF_CODE)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        refCodeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        MessageConstants.DELETED.formatted(MessageConstants.RESOURCE_REF_CODE)));
    }
}
