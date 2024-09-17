package com.example.vehiclemanager.cache;

import com.example.vehiclemanager.service.VehicleService;
import jakarta.annotation.PostConstruct;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheWarmUp {
    @Autowired
    private VehicleService vehicleService;

    @PostConstruct
    public void warmUpCache() {
        // Itt hívhatod meg a VehicleService metódust, amely betölti a járműveket a cache-be
        vehicleService.loadInitialDataToCache();
    }

}
