package com.siemens.train_ticketing.controller;

import com.siemens.train_ticketing.model.RouteSegment;
import com.siemens.train_ticketing.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @GetMapping("/fastest")
    public ResponseEntity<List<RouteSegment>> getFastestRoute(
            @RequestParam String startStationId,
            @RequestParam String endStationId
    ) {
        List<RouteSegment> route = routeService.findFastestRoute(startStationId, endStationId);

        if(route.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(route);
    }
}
