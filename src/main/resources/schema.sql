CREATE TABLE vehicle (
                         uuid UUID PRIMARY KEY,
                         rendszam VARCHAR(20) UNIQUE,
                         tulajdonos VARCHAR(200),
                         forgalmi_ervenyes DATE
);

CREATE TABLE vehicle_adatok (
                                vehicle_uuid UUID NOT NULL,
                                adatok VARCHAR(200),
                                FOREIGN KEY (vehicle_uuid) REFERENCES vehicle(uuid)
);
ALTER TABLE vehicle
ALTER COLUMN rendszam TYPE VARCHAR(20) COLLATE "C";

ALTER TABLE vehicle
ALTER COLUMN tulajdonos TYPE VARCHAR(20) COLLATE "C";

ALTER TABLE vehicle_adatok
ALTER COLUMN adatok TYPE VARCHAR(20) COLLATE "C";