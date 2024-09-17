package com.example.vehiclemanager.controller;

import com.example.vehiclemanager.dto.VehicleDTO;
import com.example.vehiclemanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/kereses")
public class SearchController {

    private final VehicleService vehicleService;

    @Autowired
    public SearchController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Async
    @GetMapping
    public CompletableFuture<ResponseEntity<List<VehicleDTO>>> searchVehicles(
            @RequestParam(value = "q", required = false) String q) {
        if (q == null || q.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A keresési kulcsszó (q) kötelező.");
        }

        return CompletableFuture.supplyAsync(() -> {
            List<VehicleDTO> vehicles = vehicleService.searchVehiclesByKeyword(q);
            return ResponseEntity.ok(vehicles);
        });
    }
}