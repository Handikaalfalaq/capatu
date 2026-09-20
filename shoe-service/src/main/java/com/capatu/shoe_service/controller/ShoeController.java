package com.capatu.shoe_service.controller;


import com.capatu.shoe_service.constant.MessageConstants;
import com.capatu.shoe_service.dto.request.ShoeRequest;
import com.capatu.shoe_service.dto.request.ShoeSearchRequest;
import com.capatu.shoe_service.dto.response.ApiResponse;
import com.capatu.shoe_service.service.RefCodeService;
import com.capatu.shoe_service.service.ShoeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/shoe")
public class ShoeController {

    private final ShoeService shoeService;

    public ShoeController(ShoeService shoeService) {
        this.shoeService = shoeService;
    }

    @GetMapping()
    public ResponseEntity<?> findAllCode() {
        return ResponseEntity.ok(shoeService.findAll());
    }

    @PostMapping("/pageable")
    public ResponseEntity<?> getPageableByFilters(@Valid @RequestBody ShoeSearchRequest request) {
        return ResponseEntity.ok(shoeService.getPageableByFilters(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(shoeService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody ShoeRequest request) {
        shoeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        MessageConstants.CREATED.formatted(MessageConstants.RESOURCE_REF_CODE)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ShoeRequest request) {
        shoeService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        MessageConstants.UPDATED.formatted(MessageConstants.RESOURCE_REF_CODE)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        shoeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        MessageConstants.DELETED.formatted(MessageConstants.RESOURCE_REF_CODE)));
    }
}
