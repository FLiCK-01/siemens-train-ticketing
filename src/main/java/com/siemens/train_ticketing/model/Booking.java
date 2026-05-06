package com.siemens.train_ticketing.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private String id;
    private String customerEmail;
    private String trainId;
    private String departureStationId;
    private String arrivalStationId;
    private int numberOfTickets;
}
