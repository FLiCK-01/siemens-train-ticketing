package com.siemens.train_ticketing.repository;

import com.siemens.train_ticketing.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, String> {
}
