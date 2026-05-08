package com.siemens.train_ticketing.service;

import com.siemens.train_ticketing.model.Booking;
import com.siemens.train_ticketing.model.Station;
import com.siemens.train_ticketing.model.Train;
import com.siemens.train_ticketing.repository.BookingRepository;
import com.siemens.train_ticketing.repository.TrainRepository;
import com.siemens.train_ticketing.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final NotificationService notificationService;

    @Transactional
    public Booking bookTicket(String email, String trainId, String depStationId, String arrStationId, int numberOfTickets) {
        Train train = trainRepository.findByIdForUpdate(trainId).orElseThrow(() -> new IllegalArgumentException(
                "Train with ID" + trainId + " not found."));
        Station departure = stationRepository.findById(depStationId).orElseThrow(() -> new IllegalArgumentException
                ("Departure Station with ID" + depStationId + " not found."));
        Station arrival = stationRepository.findById(arrStationId).orElseThrow(() -> new IllegalArgumentException
                ("Arrival Station with ID" + arrStationId + " not found."));

        List<Booking> allBookings = bookingRepository.findAll();
        int currentlyBookedSeats = allBookings.stream().filter(b -> b.getTrain().getId().equals(trainId))
                .mapToInt(Booking::getNumberOfTickets).sum();

        if(currentlyBookedSeats + numberOfTickets > train.getCapacity()) {
            throw new IllegalStateException("Overbooking detected! Not enough seats left.");
        }

        Booking booking = new Booking(null, email, train, departure, arrival, numberOfTickets);
        Booking savedBooking = bookingRepository.save(booking);

        String emailBody = String.format("Reservation confirmed: %d tickets on train %s (%s -> %s).",
                numberOfTickets, train.getName(), departure.getName(), arrival.getName());
        notificationService.sendEmail(email, "Your Siemens train tickets.",  emailBody);

        return savedBooking;
    }
}
