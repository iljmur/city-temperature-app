package com.meteo.citytemperature.service;

import com.meteo.citytemperature.model.City;
import com.meteo.citytemperature.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class CityService {
    private static final String API_KEY = "9c4f1e01c060df147afcdeda9364b627";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=" + API_KEY + "&units=metric";

    @Autowired
    private CityRepository cityRepository;

    @PostConstruct
    public void init() {
        loadCities();
    }

    public void loadCities() {
        String[] cities = {"Riga", "Tallinn", "Berlin"};

        for (String cityName : cities) {
            double temperature = getTemperatureForCity(cityName);
            City city = new City();
            city.setName(cityName);
            city.setTemperature(temperature);
            cityRepository.save(city);
        }
    }

    private double getTemperatureForCity(String cityName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(API_URL, cityName);
        CityWeatherResponse response = restTemplate.getForObject(url, CityWeatherResponse.class);
        return response.getMain().getTemp();
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public void deleteAllCities() {
        cityRepository.deleteAll();
    }

    public void reloadCities() {
        deleteAllCities();
        loadCities();
    }
}