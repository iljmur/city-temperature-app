package com.meteo.citytemperature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.meteo.citytemperature.model.City;

public interface CityRepository extends JpaRepository<City, Long> {
}