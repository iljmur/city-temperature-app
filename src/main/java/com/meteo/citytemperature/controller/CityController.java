package com.meteo.citytemperature.controller;

import com.meteo.citytemperature.model.City;
import com.meteo.citytemperature.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    @Autowired
    private CityService cityService;

    @GetMapping
    public Page<City> getAllCities(Pageable pageable) {
        return cityService.getAllCities(pageable);
    }

    @DeleteMapping
    public void deleteAllCities() {
        cityService.deleteAllCities();
    }

    @PostMapping("/reload")
    public void reloadCities() {
        cityService.reloadCities();
        cityService.updateCityTemperatures();
    }
}