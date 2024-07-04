package com.meteo.citytemperature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CityTemperatureApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityTemperatureApplication.class, args);
	}

}
