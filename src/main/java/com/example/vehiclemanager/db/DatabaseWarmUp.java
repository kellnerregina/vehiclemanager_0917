package com.example.vehiclemanager.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseWarmUp {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void warmUpDatabase() {
        System.out.println("Adatbázis előmelegítése indul...");

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // Egyszerű lekérdezés az adatbázis kapcsolat teszteléséhez
            statement.execute("SELECT * FROM vehicle where tulajdonos= 'Regina'");

            System.out.println("Adatbázis előmelegítése sikeresen befejeződött.");

        } catch (SQLException e) {
            // Hibakezelés
            System.err.println("Hiba történt az adatbázis előmelegítése során: " + e.getMessage());
            e.printStackTrace();
        }
    }
}