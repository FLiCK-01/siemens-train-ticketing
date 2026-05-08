package com.siemens.train_ticketing.controller;

import com.siemens.train_ticketing.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/trains/{trainId}/delay")
    public ResponseEntity<String> notifyDelay(
            @PathVariable String trainId,
            @RequestParam int delayMinutes) {
        try {
            adminService.reportTrainDelay(trainId, delayMinutes);
            return ResponseEntity.ok("Delay has been registered. Customers notified through email.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
