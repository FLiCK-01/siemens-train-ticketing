package com.siemens.train_ticketing.dto;

import java.time.LocalTime;

public record RouteRequest(
        String trainId,
        String departureStationId,
        String arrivalStationId,
        LocalTime departureTime,
        LocalTime arrivalTime
) {}