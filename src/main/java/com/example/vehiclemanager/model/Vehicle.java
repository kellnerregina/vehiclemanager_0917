package com.example.vehiclemanager.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "vehicle", indexes = {
        @Index(name = "idx_vehicle_rendszam", columnList = "rendszam"),
        @Index(name = "idx_vehicle_tulajdonos", columnList = "tulajdonos")
})
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(unique = true, length = 20)
    //@Size(max = 20, message = "A rendszám nem lehet több mint 20 karakter.")
    private String rendszam;

    @Column(length = 200)
    //@Size(max = 200, message = "A tulajdonosmező nem lehet több mint 200 karakter.")
    private String tulajdonos;

    @JsonProperty("forgalmi_ervenyes")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Dátum formátum kezelése
    @Column(name = "forgalmi_ervenyes") //Ez az adatbázis neve
    private LocalDate forgalmi_ervenyes;

    // Kiegészítő adatok (maximum 200 elem, egyenként maximum 200 karakter hosszúság)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vehicle_adatok", joinColumns = @JoinColumn(name = "vehicle_uuid"))
    @Column(name = "adatok")
    @Size(max = 200, message = "Legfeljebb 200 kiegészítő adat lehet.")
    private List<@Size(max = 200, message = "Minden kiegészítő adat maximum 200 karakter lehet.") String> adatok;

    // Getters and Setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRendszam() {
        return rendszam;
    }

    public void setRendszam(String rendszam) {
        this.rendszam = rendszam;
    }

    public String getTulajdonos() {
        return tulajdonos;
    }

    public void setTulajdonos(String tulajdonos) {
        this.tulajdonos = tulajdonos;
    }

    public LocalDate getForgalmi_ervenyes() {
        return forgalmi_ervenyes;
    }

    public void setForgalmi_ervenyes(LocalDate forgalmi_ervenyes) {
        this.forgalmi_ervenyes = forgalmi_ervenyes;
    }

    public List<String> getAdatok() {
        return adatok;
    }

    public void setAdatok(List<String> adatok) {
        this.adatok = adatok;
    }
}
