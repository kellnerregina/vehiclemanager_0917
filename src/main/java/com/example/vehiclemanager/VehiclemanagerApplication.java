package com.example.vehiclemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching // Cache engedélyezése
@EnableAsync  // Aszinkron feldolgozás engedélyezése
public class VehiclemanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehiclemanagerApplication.class, args);
    }

}
