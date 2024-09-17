package com.example.vehiclemanager.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

//paraméterezett konstruktor
//@JsonCreator
public class VehicleDTO {

    private String uuid;

    @NotBlank(message = "Kötelező mező")
    @NotEmpty(message = "Kötelező mező")
    @Size(min= 1, max = 20, message = "A rendszám mező kötelező, maximum 20 karakter hosszú lehet.")
    @NotNull(message = "A rendszám megadása kötelező")
    private String rendszam;

    @NotBlank(message = "Kötelező mező")
    @NotEmpty(message = "Kötelező mező")
    @NotNull(message = "A tulajdonos mező kötelező, maximum 200 karakter hosszú lehet.")
    @Size(min = 1, max = 200, message = "A tulajdonos mező kötelező, maximum 200 karakter hosszú lehet.")
    private String tulajdonos;

    @NotNull(message = "A forgalmi érvényesség dátuma nem lehet üres")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate forgalmi_ervenyes;

    @Valid
    @Size(max = 200, message = "Maximum 200 kiegészítő adat rögzíthető")
    private List<@NotBlank(message = "Az egyes adatok nem lehetnek üresek")@Size(max = 200, message = "Az adat maximum 200 karakter hosszú lehet.")String> adatok;

    @JsonCreator
    public VehicleDTO(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("rendszam") String rendszam,
            @JsonProperty("tulajdonos") String tulajdonos,
            @JsonProperty("forgalmi_ervenyes") LocalDate forgalmi_ervenyes,
            @JsonProperty("adatok") List<String> adatok) {
        this.uuid = uuid;
        this.rendszam = rendszam;
        this.tulajdonos = tulajdonos;
        this.forgalmi_ervenyes = forgalmi_ervenyes;
        this.adatok = adatok;
    }

    // Üres konstruktor
    public VehicleDTO() {
    }

    // Getter és Setter a forgalmi_ervenyes mezőhöz
    public LocalDate getForgalmi_ervenyes() {return forgalmi_ervenyes;}

    public void setForgalmi_ervenyes(LocalDate forgalmi_ervenyes) {
        this.forgalmi_ervenyes = forgalmi_ervenyes;
    }

    // További Getters és Setters a többi mezőhöz
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
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

    public List<String> getAdatok() {
        return adatok;
    }

    public void setAdatok(List<String> adatok) {
        this.adatok = adatok;
    }
}

