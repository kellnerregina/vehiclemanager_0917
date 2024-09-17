package com.example.vehiclemanager.repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.vehiclemanager.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    // Keresés rendszám, tulajdonos vagy adatok alapján
    @Query(value = "SELECT v.* " +
            "FROM vehicle v " +
            "WHERE v.search_vector @@ plainto_tsquery('hungarian', :q)",
            nativeQuery = true)
    List<Vehicle> searchByKeyword(@Param("q") String q);

    Optional<Vehicle> findFirstByRendszam(String rendszam);
}