package com.siemens.train_ticketing.controller;

import com.siemens.train_ticketing.dto.BookingRequest;
import com.siemens.train_ticketing.model.Booking;
import com.siemens.train_ticketing.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            Booking savedBooking = bookingService.bookTicket(
                    request.email(),
                    request.trainId(),
                    request.departureStationId(),
                    request.arrivalStationId(),
                    request.numberOfTickets()
            );
            return ResponseEntity.ok(savedBooking);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
