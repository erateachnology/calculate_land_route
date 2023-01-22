package com.routing_service.service.impl;

import com.routing_service.dto.RouteResponse;

public interface RouteService {
    RouteResponse calculateRoute(String origin, String destination);
}
