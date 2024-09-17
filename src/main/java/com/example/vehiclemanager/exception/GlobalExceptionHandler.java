package com.example.vehiclemanager.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import jakarta.persistence.NonUniqueResultException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Kezeli a HttpMessageNotReadableException hibát, amely a dátumformátum hibákra is vonatkozik
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("HttpMessageNotReadableException: {}", ex.getMessage());

        // Ellenőrizzük, hogy a hiba dátumformátum miatt történt-e
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
            if (invalidFormatException.getTargetType().equals(LocalDate.class)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Hibás dátumformátum! A helyes formátum: yyyy-MM-dd");
            }
        }

        // Általános hibaüzenet formátumhibára
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Érvénytelen formátum: " + ex.getMessage());
    }

    //dátum hiba
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException ex) {
        logger.error("InvalidFormatException: Hiba történt a dátum deszerializálása közben: {}", ex.getMessage());
        if (ex.getTargetType().equals(LocalDate.class)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Hibás dátumformátum! A helyes formátum: yyyy-MM-dd");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Formátum hiba: " + ex.getMessage());
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> handleJsonParseException(JsonParseException ex) {
        logger.error("JsonParseException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("JSON formátum hiba: " + ex.getOriginalMessage());
    }

    // Kezeli a ResponseStatusException kivételeket
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }

    // Kezeli a validációs hibákat (pl. request body validáció)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            // Specifikus hibaüzenetek mezőnév alapján
            //if (fieldName.equals("rendszam")) {
                errors.put(fieldName, "A rendszám nem lehet több mint 20 karakter.");
            //} else if (fieldName.equals("tulajdonos")) {
                errors.put(fieldName, "A tulajdonos neve nem lehet több mint 200 karakter.");
            //} else if (fieldName.equals("adatok")) {
                errors.put(fieldName, "Az adatok tömb nem tartalmazhat több mint 200 elemet.");
            //} else if (fieldName.startsWith("adatok[")) {
                errors.put(fieldName, "Minden kiegészítő adat maximum 200 karakter hosszú lehet.");
           // } else {
                errors.put(fieldName, errorMessage); // Alapértelmezett hibaüzenet más mezőknél
            //}
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Kezeli az adatbázis integritás megsértését (pl. egyediség megszorítás)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Már létezik ilyen rendszámú jármű!";
        logger.error("Adatbázis integritás megsértése: {}", ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<String> handleVehicleNotFoundException(VehicleNotFoundException ex) {
        logger.error("Nem található jármű: {}", ex.getMessage()); // Logoljuk a kivételt
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // HTTP 400-at adunk vissza
    }


    // Kezeli az egyedi rendszám megsértését (IllegalStateException)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        logger.error("Egyedi megszorítás megsértése: {}", ex.getMessage()); // Logoljuk a kivételt
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // HTTP 400-at adunk vissza
    }

    // Kezeli a nem egyedi eredményt (NonUniqueResultException)
    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<String> handleNonUniqueResultException(NonUniqueResultException ex) {
        logger.error("Nem egyedi eredmény: {}", ex.getMessage()); // Logoljuk a kivételt
        return new ResponseEntity<>("Nem egyedi eredmény: több rekord található ugyanazzal a rendszámmal!", HttpStatus.BAD_REQUEST);
    }

    // Általános kivételkezelő (HTTP 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        logger.error("Általános kivétel: {}", ex.getMessage());
        return new ResponseEntity<>("Hiba történt: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
