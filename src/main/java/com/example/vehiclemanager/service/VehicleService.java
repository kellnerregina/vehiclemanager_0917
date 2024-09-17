package com.example.vehiclemanager.service;

import com.example.vehiclemanager.dto.VehicleDTO;
import com.example.vehiclemanager.exception.VehicleNotFoundException;
import com.example.vehiclemanager.mapper.VehicleMapper;
import com.example.vehiclemanager.model.Vehicle;
import com.example.vehiclemanager.repository.VehicleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public VehicleDTO getVehicleByUuid(UUID uuid) {
        Vehicle vehicle = vehicleRepository.findById(uuid)
                .orElseThrow(() -> new VehicleNotFoundException("A jármű nem található ezzel az azonosítóval: " + uuid));
        return vehicleMapper.toDTO(vehicle);
    }


    @Transactional
    public VehicleDTO addVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);

        Optional<Vehicle> existingVehicle = vehicleRepository.findFirstByRendszam(vehicle.getRendszam());
        if (existingVehicle.isPresent()) {
            throw new DataIntegrityViolationException("Már létezik ilyen rendszámú jármű: " + vehicle.getRendszam());
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return (vehicleMapper.toDTO(savedVehicle));
    }

    public long getVehicleCount() {
        return vehicleRepository.count();

    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> searchVehiclesByKeyword(String q) {
        List<Vehicle> vehicles = vehicleRepository.searchByKeyword(q);
        return vehicles.stream().map(vehicleMapper::toDTO).collect(Collectors.toList());
    }
}