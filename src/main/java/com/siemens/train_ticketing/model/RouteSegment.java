package com.siemens.train_ticketing.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteSegment {
    public String trainId;
    public String departureStationId;
    public String arrivalStationId;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
}
