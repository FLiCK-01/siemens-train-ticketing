package com.siemens.train_ticketing.seeder;

import com.siemens.train_ticketing.model.RouteSegment;
import com.siemens.train_ticketing.model.Station;
import com.siemens.train_ticketing.model.Train;
import com.siemens.train_ticketing.repository.RouteSegmentRepository;
import com.siemens.train_ticketing.repository.StationRepository;
import com.siemens.train_ticketing.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final StationRepository stationRepository;
    private final TrainRepository trainRepository;
    private final RouteSegmentRepository routeSegmentRepository;

    @Override
    public void run(String... args) throws Exception {
        Station cluj = new Station("CJ", "Cluj-Napoca");
        Station brasov = new Station("BV", "Brasov");
        Station bucuresti = new Station("B", "Bucuresti");
        Station constanta = new Station("CT", "Constanta");

        stationRepository.saveAll(List.of(cluj, brasov, bucuresti, constanta));

        Train train1 = new Train("IR-100", "InterRegio 100", 50);
        Train train2 = new Train("R-200", "Regio 200", 100);

        trainRepository.saveAll(List.of(train1, train2));

        RouteSegment seg1 = new RouteSegment(null, train1, cluj, brasov,
                LocalTime.of(8, 0), LocalTime.of(13, 0));

        RouteSegment seg2 = new RouteSegment(null, train1, brasov, bucuresti,
                LocalTime.of(13, 15), LocalTime.of(16, 0));

        RouteSegment seg3 = new RouteSegment(null, train2, brasov, constanta,
                LocalTime.of(14, 0), LocalTime.of(19, 0));

        routeSegmentRepository.saveAll(List.of(seg1, seg2, seg3));

        System.out.println("database successfully populated.");
    }
}