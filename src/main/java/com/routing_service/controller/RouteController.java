package com.routing_service.controller;
import com.routing_service.service.impl.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {

 private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/routing/{origin}/{destination}")
    ResponseEntity getRoute(@PathVariable("origin") String origin,
                                                       @PathVariable("destination") String destination) {
        return new ResponseEntity<>(routeService.calculateRoute(origin,destination), HttpStatus.OK);

    }

}
