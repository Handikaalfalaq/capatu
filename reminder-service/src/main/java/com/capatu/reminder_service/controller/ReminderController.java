package com.capatu.reminder_service.controller;

import com.capatu.reminder_service.constant.Constants;
import com.capatu.reminder_service.dto.response.ApiResponse;
import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderStatus;
import com.capatu.reminder_service.service.ReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping()
    public ResponseEntity<?> findAll(@RequestParam(required = false) ReminderStatus status,
                                     @RequestParam(required = false) ReminderSeverity severity) {
        return ResponseEntity.ok(reminderService.findAll(status, severity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reminderService.findById(id));
    }

    @PostMapping("/{id}/acknowledge")
    public ResponseEntity<?> acknowledge(@PathVariable Long id) {
        reminderService.acknowledge(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        Constants.ACKNOWLEDGED.formatted(Constants.RESOURCE_REMINDER)));
    }

    @PostMapping("/evaluate")
    public ResponseEntity<?> evaluate() {
        return ResponseEntity.ok(reminderService.evaluate());
    }
}
