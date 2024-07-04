package com.meteo.citytemperature.scheduler;

import com.meteo.citytemperature.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherUpdateScheduler {
    private static final Logger logger = LoggerFactory.getLogger(WeatherUpdateScheduler.class);

    @Autowired
    private CityService cityService;

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void updateWeatherData() {
        logger.info("Updating city temperatures ...");
        cityService.updateCityTemperatures();
        logger.info("Finished updating city temperatures");
    }
}