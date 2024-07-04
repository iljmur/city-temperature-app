package com.meteo.citytemperature.service;

import com.meteo.citytemperature.model.City;
import com.meteo.citytemperature.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {
    private static final String API_KEY = "9c4f1e01c060df147afcdeda9364b627";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=" + API_KEY + "&units=metric";
    private static final String API_CITIES_URL = "https://restcountries.com/v3.1/all?fields=capital,unMember";

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
        }
    }

    public void updateCityTemperatures() {
        List<City> allCities = cityRepository.findAll();
        for (City city : allCities) {
            double temperature = getTemperatureForCity(city.getName());
            city.setTemperature(temperature);
            cityRepository.save(city);
        }
    }

    private List<String> getCapitalNames() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(API_CITIES_URL, String.class);

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
        String url = String.format(API_URL, cityName);
        CityWeatherResponse response = restTemplate.getForObject(url, CityWeatherResponse.class);
        return response.getMain().getTemp();
    }

    public Page<City> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    public void deleteAllCities() {
        cityRepository.deleteAll();
    }

    public void reloadCities() {
        deleteAllCities();
        loadCities();
    }
}