package com.siemens.train_ticketing.service;

import com.siemens.train_ticketing.model.RouteSegment;
import com.siemens.train_ticketing.repository.RouteSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteSegmentRepository routeSegmentRepository;

    private static class RouteState {
        List<RouteSegment> path;
        long totalDurationMinutes;

        public RouteState(List<RouteSegment> path, long totalDurationMinutes) {
            this.path = path;
            this.totalDurationMinutes = totalDurationMinutes;
        }
    }

    public List<RouteSegment> findFastestRoute(String startStationId, String endStationId) {
        List<RouteSegment> allSegments = routeSegmentRepository.findAll();

        PriorityQueue<RouteState> queue = new PriorityQueue<>(
                Comparator.comparingLong(state -> state.totalDurationMinutes)
        );
        Map<String, Long> bestTimesToStation = new HashMap<>();

        for (RouteSegment segment : allSegments) {
            if (segment.getDepartureStation().getId().equals(startStationId)) {
                long travelTime = Duration.between(segment.getDepartureTime(), segment.getArrivalTime()).toMinutes();
                if (travelTime < 0) travelTime += 24 * 60;

                List<RouteSegment> initialPath = new ArrayList<>();
                initialPath.add(segment);
                queue.add(new RouteState(initialPath, travelTime));
            }
        }

        while (!queue.isEmpty()) {
            RouteState currentState = queue.poll();
            RouteSegment lastSegment = currentState.path.get(currentState.path.size() - 1);
            String currentStationId = lastSegment.getArrivalStation().getId();

            if (currentStationId.equals(endStationId)) {
                return currentState.path;
            }

            if (bestTimesToStation.getOrDefault(currentStationId, Long.MAX_VALUE) <= currentState.totalDurationMinutes) {
                continue;
            }
            bestTimesToStation.put(currentStationId, currentState.totalDurationMinutes);

            for (RouteSegment nextSegment : allSegments) {
                if (nextSegment.getDepartureStation().getId().equals(currentStationId)) {

                    if (nextSegment.getDepartureTime().isAfter(lastSegment.getArrivalTime())) {
                        long waitTime = Duration.between(lastSegment.getArrivalTime(), nextSegment.getDepartureTime()).toMinutes();
                        long travelTime = Duration.between(nextSegment.getDepartureTime(), nextSegment.getArrivalTime()).toMinutes();
                        if (travelTime < 0) travelTime += 24 * 60;

                        long newTotalDuration = currentState.totalDurationMinutes + waitTime + travelTime;

                        List<RouteSegment> newPath = new ArrayList<>(currentState.path);
                        newPath.add(nextSegment);
                        queue.add(new RouteState(newPath, newTotalDuration));
                    }
                }
            }
        }

        return Collections.emptyList();
    }        public List<RouteSegment> getAllSegmentsForDebug() {
        return routeSegmentRepository.findAll();
        }
}
