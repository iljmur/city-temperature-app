package com.meteo.citytemperature.controller;

import com.meteo.citytemperature.model.City;
import com.meteo.citytemperature.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    @Autowired
    private CityService cityService;

    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @DeleteMapping
    public void deleteAllCities() {
        cityService.deleteAllCities();
    }

    @PostMapping("/reload")
    public void reloadCities() {
        cityService.reloadCities();
    }
}