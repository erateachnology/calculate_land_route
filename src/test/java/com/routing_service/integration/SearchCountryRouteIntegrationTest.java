package com.routing_service.integration;
import com.routing_service.dto.RouteResponse;
import com.routing_service.exception.RouteNotFoundException;
import com.routing_service.service.impl.RouteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SearchCountryRouteIntegrationTest {

    @Autowired
    private RouteService routeService;

    @Test
    @DisplayName("Calculate route between CZE to ITA")
    public void calculateRouteTest(){
        RouteResponse response =  routeService.calculateRoute("CZE", "ITA");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(3,response.getRoute().size());
        Assertions.assertEquals("CZE", response.getRoute().get(0));
    }

    @Test
    @DisplayName("Calculate route when origin country and destination country equals")
    public void calculateRouteOriginDestinationEqualTest(){
        RouteNotFoundException thrown =   Assertions.assertThrows(RouteNotFoundException.class, () -> {
            routeService.calculateRoute("CZE", "CZE");
        },"Origin and target country codes are equal");

         Assertions.assertTrue(thrown.getMessage().contentEquals("Origin and target country codes are equal"));
    }

    @Test
    @DisplayName("Calculate route when origin country is not there")
    public void calculateRouteOriginNullTest(){
        RouteNotFoundException thrown =   Assertions.assertThrows(RouteNotFoundException.class, () -> {
            routeService.calculateRoute(null, "CZE");
        },"Origin country could not found");

        Assertions.assertTrue(thrown.getMessage().contentEquals("Origin country could not found"));
    }

    @Test
    @DisplayName("Calculate route when destination country is not there")
    public void calculateRouteDestinationNullTest(){
        RouteNotFoundException thrown =   Assertions.assertThrows(RouteNotFoundException.class, () -> {
            routeService.calculateRoute("CZE", null);
        },"Target country could not found");

        Assertions.assertTrue(thrown.getMessage().contentEquals("Target country could not found"));
    }


}
