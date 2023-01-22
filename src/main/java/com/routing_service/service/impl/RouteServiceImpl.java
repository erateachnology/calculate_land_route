package com.routing_service.service.impl;

import com.routing_service.dto.Country;
import com.routing_service.dto.RouteResponse;
import com.routing_service.exception.RouteNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RouteServiceImpl implements RouteService {
    private static final String ORIGIN_COUNTRY_COULD_NOT_FOUND = "Origin country could not found";
    private static final String TARGET_COUNTRY_COULD_NOT_FOUND = "Target country could not found";
    private static final String ORIGIN_AND_TARGET_COUNTRY_CODES_ARE_EQUAL = "Origin and target country codes are equal";
    private static final String ORIGIN_COUNTRY_S_BORDERS_ARE_EMPTY = "Origin country's borders are empty";
    private static final String TARGET_COUNTRY_S_BORDERS_ARE_EMPTY = "Target country's borders are empty";
    @Value("${country.url}")
    private String countryUrl;

    private final RestTemplate restTemplate;

    public RouteServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);

        this.restTemplate.setMessageConverters(messageConverters);
    }

    @Override
    public RouteResponse calculateRoute(String origin, String destination){
       log.info("Calculate route start from {} to {}", origin, destination);
        // Get Country List
        Map<String, Country> countriesMap = getCountryMap();
        log.debug("countries {}", countriesMap.size());

        //Validate Countries
        Country originCountry = countriesMap.get(origin);
        log.debug("origin country {}", originCountry);
        Country targetCountry = countriesMap.get(destination);
        log.debug("target country {}", destination);
        validateCountries(originCountry, targetCountry, origin, destination);

        List<String> route = new SearchImpl(countriesMap, originCountry, targetCountry).search();
        log.debug("route {}" , route);
        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setRoute(route);

        log.info("Countries route found success");
        return routeResponse;
    }

    public void validateCountries(Country originCountry, Country targetCountry, String origin, String destination){
        if (originCountry == null){
            log.error(ORIGIN_COUNTRY_COULD_NOT_FOUND);
            throw new RouteNotFoundException(ORIGIN_COUNTRY_COULD_NOT_FOUND);
        }
        if (targetCountry == null){
            log.error(TARGET_COUNTRY_COULD_NOT_FOUND);
            throw new RouteNotFoundException(TARGET_COUNTRY_COULD_NOT_FOUND);
        }

        if(originCountry.equals(targetCountry)){
            log.error(ORIGIN_AND_TARGET_COUNTRY_CODES_ARE_EQUAL);
            throw new RouteNotFoundException(ORIGIN_AND_TARGET_COUNTRY_CODES_ARE_EQUAL);
        }
        if (originCountry.getBorders().isEmpty()) {
            log.error(ORIGIN_COUNTRY_S_BORDERS_ARE_EMPTY);
            throw new RouteNotFoundException(ORIGIN_COUNTRY_S_BORDERS_ARE_EMPTY);
        }
        if (targetCountry.getBorders().isEmpty()) {
            log.error(TARGET_COUNTRY_S_BORDERS_ARE_EMPTY);
            throw new RouteNotFoundException(TARGET_COUNTRY_S_BORDERS_ARE_EMPTY);
        }
    }

    public Map<String, Country> getCountryMap(){
        Country[]  countriesArray =  restTemplate.getForObject(countryUrl, Country[].class);
        List<Country> countries =  Arrays.asList(countriesArray);
       return countries.stream()
                .collect(Collectors.toMap(Country::getCca3, Function.identity()));
    }
}
