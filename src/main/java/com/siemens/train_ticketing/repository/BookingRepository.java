package com.siemens.train_ticketing.repository;

import com.siemens.train_ticketing.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByTrainId(String trainId);
}
