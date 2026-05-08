package com.siemens.train_ticketing.controller;

import com.siemens.train_ticketing.model.Train;
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

    @GetMapping("/trains/{trainId}/bookings")
    public ResponseEntity<?> getTrainBookings(@PathVariable String trainId) {
        return ResponseEntity.ok(adminService.getBookingsForTrain(trainId));
    }

    @PostMapping("/trains")
    public ResponseEntity<?> addTrain(@RequestBody Train train) {
        try {
            Train savedTrain = adminService.createTrain(train);
            return ResponseEntity.ok(savedTrain);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/trains/{trainId}")
    public ResponseEntity<?> deleteTrain(@PathVariable String trainId) {
        try {
            adminService.deleteTrain(trainId);
            return ResponseEntity.ok("Train has been deleted.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
