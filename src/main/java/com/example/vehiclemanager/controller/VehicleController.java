package com.example.vehiclemanager.controller;

import com.example.vehiclemanager.dto.VehicleDTO;
import com.example.vehiclemanager.mapper.VehicleMapper;
import com.example.vehiclemanager.model.Vehicle;
import com.example.vehiclemanager.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/jarmuvek")
public class VehicleController {


    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;

    // Konstruktor alapú injektálás
    @Autowired
    public VehicleController(VehicleService vehicleService, VehicleMapper vehicleMapper) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
    }

    @GetMapping("/vehicles")
    public List<Vehicle> getVehicles() {
        return vehicleService.findAll();
    }

    // GET /jarmuvek - Járművek számának lekérdezése
    @GetMapping(produces = "text/plain")
    public ResponseEntity<String> getVehicleCount() {
        long vehicleCount = vehicleService.getVehicleCount();
        return ResponseEntity.ok(String.valueOf(vehicleCount)); // Szám visszaadása text/plain formátumban
    }

    // GET /jarmuvek/{uuid} - Jármű lekérdezése UUID alapján
    @GetMapping("/{uuid}")
    public ResponseEntity<VehicleDTO> getVehicleByUuid(@PathVariable UUID uuid) {
        VehicleDTO vehicle = vehicleService.getVehicleByUuid(uuid);
        if (vehicle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicle);
    }

    // POST /jarmuvek - Jármű létrehozása

    @PostMapping
    public ResponseEntity<VehicleDTO> addVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {

            // Az új jármű mentése aszinkron módon
            VehicleDTO savedVehicleDTO = vehicleService.addVehicle(vehicleDTO);  // Visszakapjuk a mentett járművet

            // Itt használjuk a savedVehicleDTO UUID-jét a location URI-hoz
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{uuid}")
                    .buildAndExpand(savedVehicleDTO.getUuid())  // A mentett jármű UUID-je
                    .toUri();

            // HTTP 201 Created válasz a hely URI-val és a mentett jármű adataival
            return ResponseEntity.created(location).body(savedVehicleDTO);
        }
}
