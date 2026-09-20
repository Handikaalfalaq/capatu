package com.capatu.shoe_service.controller;

import com.capatu.shoe_service.service.StatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<?> summary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(statsService.summary(from, to));
    }

    @GetMapping("/ranking")
    public ResponseEntity<?> ranking(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(statsService.ranking(from, to));
    }

    @GetMapping("/rotation")
    public ResponseEntity<?> rotation(@RequestParam(required = false) Integer days) {
        return ResponseEntity.ok(statsService.rotation(days));
    }

    @GetMapping("/shoe-life")
    public ResponseEntity<?> shoeLife(@RequestParam(required = false) BigDecimal threshold) {
        return ResponseEntity.ok(statsService.shoeLife(threshold));
    }
}
