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

    @PutMapping("/trains/{trainId}")
    public ResponseEntity<?> updateTrain(@PathVariable String trainId, @RequestBody com.siemens.train_ticketing.dto.UpdateTrainRequest request) {
        try {
            return ResponseEntity.ok(adminService.updateTrain(trainId, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/stations")
    public ResponseEntity<?> addStation(@RequestBody com.siemens.train_ticketing.model.Station station) {
        return ResponseEntity.ok(adminService.createStation(station));
    }

    @PostMapping("/routes")
    public ResponseEntity<?> addRouteSegment(@RequestBody com.siemens.train_ticketing.dto.RouteRequest request) {
        try {
            return ResponseEntity.ok(adminService.createRouteSegment(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
