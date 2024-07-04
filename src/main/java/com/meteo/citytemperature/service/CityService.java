package com.meteo.citytemperature.service;

import com.meteo.citytemperature.model.City;
import com.meteo.citytemperature.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {
    private static final Logger logger = LoggerFactory.getLogger(CityService.class);
    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${countries.api.url}")
    private String countriesApiUrl;

    @Autowired
    private CityRepository cityRepository;

    @PostConstruct
    public void init() {
        if (cityRepository.count() < 100) {
            reloadCities();
        }
        updateCityTemperatures();
    }

    public void loadCities() {
        List<String> cities = getCapitalNames();

        for (String cityName : cities) {
            City city = new City();
            city.setName(cityName);
            cityRepository.save(city);
            logger.info("Loaded city: {}", cityName);
        }
    }

    public void updateCityTemperatures() {
        List<City> allCities = cityRepository.findAll();
        for (City city : allCities) {
            double temperature = getTemperatureForCity(city.getName());
            city.setTemperature(temperature);
            cityRepository.save(city);
            logger.info("Updated temperature for city: {} to {}", city.getName(), temperature);
        }
    }

    private List<String> getCapitalNames() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(countriesApiUrl, String.class);

        List<String> capitals = new ArrayList<>();
        if (response != null) {
            JSONArray countries = new JSONArray(response);
            for (int i = 0; i < countries.length(); i++) {
                JSONObject country = countries.getJSONObject(i);
                JSONArray capitalArray = country.optJSONArray("capital");
                Boolean isUnMember = (Boolean) country.get("unMember");
                if (isUnMember && capitalArray != null && capitalArray.length() > 0) {
                    capitals.add(capitalArray.getString(0));
                }
                if (capitals.size() >= 100) {
                    break;
                }
            }
        }
        return capitals;
    }

    private double getTemperatureForCity(String cityName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(weatherApiUrl, cityName);
        CityWeatherResponse response = restTemplate.getForObject(url, CityWeatherResponse.class);
        return response.getMain().getTemp();
    }

    public Page<City> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    public void deleteAllCities() {
        cityRepository.deleteAll();
        logger.info("All cities have been deleted from the database.");
    }

    public void reloadCities() {
        deleteAllCities();
        loadCities();
        logger.info("Cities have been reloaded.");
    }
}