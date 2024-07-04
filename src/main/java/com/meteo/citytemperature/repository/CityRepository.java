package com.meteo.citytemperature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.meteo.citytemperature.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityRepository extends JpaRepository<City, Long> {
    Page<City> findAll(Pageable pageable);
}