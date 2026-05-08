package com.siemens.train_ticketing.service;

import com.siemens.train_ticketing.model.Booking;
import com.siemens.train_ticketing.model.Train;
import com.siemens.train_ticketing.repository.BookingRepository;
import com.siemens.train_ticketing.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    public void reportTrainDelay(String trainId, int delayMinutes) {
        Train train = trainRepository.findById(trainId).orElseThrow(() -> new IllegalArgumentException
                ("Train with id " + trainId + " not found."));

        List<Booking> affectedBookings = bookingRepository.findByTrainId(trainId);

        if(affectedBookings.isEmpty()){
            System.out.println("Train has a delay but there are no affected customers.");
            return;
        }

        for(Booking booking : affectedBookings) {
            String subject = "Attention! Delay for train: " + train.getName();
            String body = String.format(
                    "Hello, \n\nWe wish to inform you that the train %s on the route %s -> %s will have an estimated delay of %d minutes." +
                            "\n We are deeply sorry for this unfortunate event.",
                    train.getName(),
                    booking.getDepartureStationId().getName(),
                    booking.getArrivalStationId().getName(),
                    delayMinutes
            );

            notificationService.sendEmail(booking.getCustomerEmail(), subject, body);
        }
    }

    public List<Booking> getBookingsForTrain(String trainId) {
        return bookingRepository.findByTrainId(trainId);
    }

    public Train createTrain(Train train) {
        if(trainRepository.existsById(train.getId())){
            throw new IllegalArgumentException("Train with id " + train.getId() + " already exists.");
        }

        return trainRepository.save(train);
    }

    public void deleteTrain(String trainId) {
        if(!trainRepository.existsById(trainId)){
            throw new IllegalArgumentException("Train with id " + trainId + " not found.");
        }
        trainRepository.deleteById(trainId);
    }
}
