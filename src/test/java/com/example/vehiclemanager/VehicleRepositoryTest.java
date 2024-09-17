package com.example.vehiclemanager;

import com.example.vehiclemanager.model.Vehicle;
import com.example.vehiclemanager.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test") // Speciális tesztprofil használata
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    @Transactional
    public void testSaveVehicleWithAdatok() {
        // Új Vehicle létrehozása
        Vehicle vehicle = new Vehicle();
        vehicle.setRendszam("ABC123");
        vehicle.setTulajdonos("Teszt Tulajdonos");
        vehicle.setForgalmi_ervenyes(LocalDate.now());

        // Adatok hozzáadása
        vehicle.setAdatok(Arrays.asList("adat1", "adat2"));

        // Vehicle mentése
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Ellenőrizzük, hogy sikerült-e menteni
        assertNotNull(savedVehicle.getUuid());

        // Esetleg további ellenőrzések (pl. hogy az adatok helyesen mentek-e)
        assertNotNull(savedVehicle.getAdatok());
        System.out.println("Vehicle saved successfully with adatok: " + savedVehicle.getAdatok());
    }
}
