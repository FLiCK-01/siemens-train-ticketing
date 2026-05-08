package com.siemens.train_ticketing.dto;

public record BookingRequest(
        String email,
        String trainId,
        String departureStationId,
        String arrivalStationId,
        int numberOfTickets
) {
}
