package com.routing_service.service.impl;

import com.routing_service.dto.Country;
import com.routing_service.exception.RouteNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class SearchImpl implements Search{

    private final Map<String, Country> countries;
    private final Country origin;
    private final Country target;


    private final Map<Country, Boolean> visitedCountryMap = new HashMap<>();
    private final Map<Country, Country> previousPathMap = new HashMap<>();



    @Override
    public List<String> search() {
        log.info("Route search start");
        Queue<Country> queue = new LinkedList<>();
        queue.add(origin);

        boolean check = false;

        while (!queue.isEmpty()) {
            Country currentCountry = queue.remove();
            visitedCountryMap.put(currentCountry, true);
            if (target.equals(currentCountry)) {
                log.info("current country {} and target country {} is same", currentCountry, target);
                check = true;
                break;
            }
            for (String borderCountry : currentCountry.getBorders()) {
                var neighbourCountry = countries.get(borderCountry);
                if (neighbourCountry == null){
                    log.info("neighbourCountry could not found");
                    continue;
                }
                if (!visitedCountryMap.containsKey(neighbourCountry)) {
                    queue.add(neighbourCountry);
                    visitedCountryMap.put(neighbourCountry, true);
                    previousPathMap.put(neighbourCountry, currentCountry);
                    if (neighbourCountry.equals(target)) {
                        check = true;
                        break;
                    }
                }
            }
            if (check) {
                break;
            }
        }
        if (!check) {
            log.error("Route could not be created");
            throw new RouteNotFoundException("Route could not be created");
        }

        List<Country> landRoute = new ArrayList<>();
        for (Country country = target; country != null; country = previousPathMap.get(country)) {
            landRoute.add(country);
        }

        Collections.reverse(landRoute);
        return landRoute.stream().map(Country::getCca3).collect(Collectors.toList());
    }
}
