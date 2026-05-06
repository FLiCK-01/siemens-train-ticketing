package com.siemens.train_ticketing.repository;

import com.siemens.train_ticketing.model.RouteSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteSegmentRepository extends JpaRepository<RouteSegment, String> {
}
