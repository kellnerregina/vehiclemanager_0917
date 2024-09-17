package com.example.vehiclemanager.mapper;

import com.example.vehiclemanager.dto.VehicleDTO;
import com.example.vehiclemanager.model.Vehicle;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VehicleMapper {

    // DTO-ból Modelbe konvertálás
    public Vehicle toEntity(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        if (dto.getUuid() != null) {
            vehicle.setUuid(UUID.fromString(dto.getUuid()));  // String-ből UUID-be
        }
        vehicle.setRendszam(dto.getRendszam());
        vehicle.setTulajdonos(dto.getTulajdonos());
        vehicle.setForgalmi_ervenyes(dto.getForgalmi_ervenyes());
        vehicle.setAdatok(dto.getAdatok());
        return vehicle;
    }

    // Modelből DTO-ba konvertálás
    public VehicleDTO toDTO(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getUuid() != null ? vehicle.getUuid().toString() : null,  // UUID-ből String-be
                vehicle.getRendszam(),
                vehicle.getTulajdonos(),
                vehicle.getForgalmi_ervenyes(),
                vehicle.getAdatok()
        );
    }
}
