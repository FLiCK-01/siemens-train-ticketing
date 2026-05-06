package com.siemens.train_ticketing.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String customerEmail;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;
    @ManyToOne
    private Station departureStationId;

    @ManyToOne
    private Station arrivalStationId;

    private int numberOfTickets;
}
