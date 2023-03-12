package fr.bred.example.interview.controller;

import fr.bred.example.interview.entities.City;
import fr.bred.example.interview.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    public CityService cityService;

    @GetMapping
    public ResponseEntity<List<City>> getCities(
            @RequestParam(name = "namePattern", defaultValue = "*") String namePattern,
            @RequestParam(name = "zipCodePattern", defaultValue = "*") String zipCodePattern,
            @RequestParam(name = "_limit", required = false) Integer limit,
            @RequestParam(name = "_start", defaultValue = "0") Integer start,
            @RequestParam(name = "_sort", required = false) String sort,
            @RequestParam(name = "_order", defaultValue = "desc") String order) {

        try {
            List<City> cities = cityService.searchCities(namePattern, zipCodePattern, limit, start, sort, order);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("X-Total-Count", String.valueOf(cities.size()));
            return ResponseEntity.ok().headers(responseHeaders).body(cities);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/nearest")
    public ResponseEntity<City> getNearestCity(@RequestParam(name = "x") String x,
                                               @RequestParam(name = "y") String y) {
        try {
            City city = cityService.getNearestCity(x, y);
            return ResponseEntity.ok().body(city);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}