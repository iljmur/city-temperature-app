package com.meteo.citytemperature.service;

import com.meteo.citytemperature.model.City;
import com.meteo.citytemperature.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTests {

    @Mock
    private CityRepository cityRepository;
    @Spy
    @InjectMocks
    private CityService cityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadCities() {
        List<String> cities = Arrays.asList("City1", "City2", "City3");
        doReturn(cities).when(cityService).getCapitalNames();

        cityService.loadCities();

        verify(cityRepository, times(cities.size())).save(any(City.class));
    }

    @Test
    public void testUpdateCityTemperatures() {
        List<City> cities = new ArrayList<>();
        City city = new City();
        city.setName("City1");
        cities.add(city);
        when(cityRepository.findAll()).thenReturn(cities);
        doReturn(25.0).when(cityService).getTemperatureForCity(anyString());

        cityService.updateCityTemperatures();

        verify(cityRepository, times(1)).save(any(City.class));
        assertEquals(25.0, city.getTemperature());
    }

    @Test
    public void testDeleteAllCities() {
        cityService.deleteAllCities();

        verify(cityRepository, times(1)).deleteAll();
    }

    @Test
    public void testReloadCities() {
        doNothing().when(cityService).deleteAllCities();
        doNothing().when(cityService).loadCities();

        cityService.reloadCities();

        verify(cityService, times(1)).deleteAllCities();
        verify(cityService, times(1)).loadCities();
    }
}
