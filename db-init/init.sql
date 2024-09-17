-- Ellenőrzi, hogy létezik e az adatbázis, hanem, hozza létre
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'jarmuvek_db') THEN
       CREATE DATABASE jarmuvek_db;
END IF;
END $$;
-- Ellenőrizze, hogy létezik-e a felhasználó, ha nem, hozza létre
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'jarmuvek_user') THEN
      CREATE USER jarmuvek_user WITH PASSWORD 'Almfa_123';
END IF;
END $$;

-- Jogosultságok megadása a felhasználónak
GRANT ALL PRIVILEGES ON DATABASE jarmuvek_db TO jarmuvek_user;

-- Táblák és indexek létrehozása
CREATE TABLE IF NOT EXISTS vehicle (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    rendszam VARCHAR(20) UNIQUE NOT NULL,
    tulajdonos VARCHAR(200),
    forgalmi_ervenyes DATE,
    search_vector tsvector,  -- Adding search_vector column for full-text search
    CONSTRAINT idx_vehicle_rendszam UNIQUE (rendszam)
    );

-- Indexek létrehozása
CREATE INDEX IF NOT EXISTS idx_vehicle_rendszam ON vehicle (rendszam);
CREATE INDEX IF NOT EXISTS idx_vehicle_tulajdonos ON vehicle (tulajdonos);
CREATE INDEX IF NOT EXISTS idx_vehicle_search_vector ON vehicle USING GIN (search_vector); -- Index for full-text search

-- Tábla a kiegészítő adatokhoz
CREATE TABLE IF NOT EXISTS vehicle_adatok (
                                              vehicle_uuid UUID REFERENCES vehicle(uuid) ON DELETE CASCADE,
    adatok VARCHAR(200),
    CONSTRAINT vehicle_adatok_size CHECK (adatok IS NULL OR LENGTH(adatok) <= 200)
    );

-- Indexek a kiegészítő adatokhoz
CREATE INDEX IF NOT EXISTS idx_vehicle_adatok ON vehicle_adatok (vehicle_uuid);

-- Jogosultságok a public sémára
UPDATE vehicle SET search_vector = to_tsvector('hungarian', rendszam || ' ' || tulajdonos);

-- Jogosultságok a public sémára
GRANT ALL PRIVILEGES ON SCHEMA public TO jarmuvek_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO jarmuvek_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO jarmuvek_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO jarmuvek_user;

-- CREATE INDEX idx_vehicle_rendszam ON vehicle (rendszam);
-- CREATE INDEX idx_vehicle_tulajdonos ON vehicle (tulajdonos);
-- CREATE INDEX idx_vehicle_adatok_adatok ON vehicle_adatok (adatok);

CREATE INDEX idx_vehicle_search ON vehicle USING gin (to_tsvector('pg_catalog.hungarian', rendszam || ' ' || tulajdonos));
CREATE INDEX idx_vehicle_adatok_search ON vehicle_adatok USING gin (to_tsvector('pg_catalog.hungarian', adatok));

-- Drop existing triggers and functions if they exist
DROP TRIGGER IF EXISTS vehicle_search_vector_trigger ON vehicle;
DROP FUNCTION IF EXISTS update_vehicle_search_vector();

DROP TRIGGER IF EXISTS vehicle_adatok_search_vector_trigger ON vehicle_adatok;
DROP FUNCTION IF EXISTS update_vehicle_search_vector_from_adatok();

-- Function to update the search vector for the 'vehicle' table based on 'rendszam', 'tulajdonos', and related 'adatok'
CREATE OR REPLACE FUNCTION update_vehicle_search_vector()
RETURNS trigger AS $$
DECLARE
adatok_concat TEXT;
BEGIN
  -- Concatenate all related 'adatok' fields from 'vehicle_adatok' table
SELECT string_agg(va.adatok, ' ')
INTO adatok_concat
FROM vehicle_adatok va
WHERE va.vehicle_uuid = NEW.uuid;

-- Update the 'search_vector' using 'rendszam', 'tulajdonos', and concatenated 'adatok'
NEW.search_vector := to_tsvector('hungarian', COALESCE(NEW.rendszam, '') || ' ' || COALESCE(NEW.tulajdonos, '') || ' ' || COALESCE(adatok_concat, ''));

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update search_vector before insert or update on the 'vehicle' table
CREATE TRIGGER vehicle_search_vector_trigger
    BEFORE INSERT OR UPDATE ON vehicle
                         FOR EACH ROW EXECUTE FUNCTION update_vehicle_search_vector();

-- Function to update the 'vehicle' search_vector when 'vehicle_adatok' is inserted, updated, or deleted
CREATE OR REPLACE FUNCTION update_vehicle_search_vector_from_adatok()
RETURNS trigger AS $$
DECLARE
adatok_concat TEXT;
BEGIN
  -- Concatenate all related 'adatok' fields from 'vehicle_adatok' table
SELECT string_agg(va.adatok, ' ')
INTO adatok_concat
FROM vehicle_adatok va
WHERE va.vehicle_uuid = COALESCE(NEW.vehicle_uuid, OLD.vehicle_uuid);

-- Update the 'search_vector' in the 'vehicle' table using 'rendszam', 'tulajdonos', and concatenated 'adatok'
UPDATE vehicle
SET search_vector = to_tsvector('hungarian', COALESCE(rendszam, '') || ' ' || COALESCE(tulajdonos, '') || ' ' || COALESCE(adatok_concat, ''))
WHERE uuid = COALESCE(NEW.vehicle_uuid, OLD.vehicle_uuid);

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update the search vector in 'vehicle' when 'vehicle_adatok' is inserted, updated, or deleted
CREATE TRIGGER vehicle_adatok_search_vector_trigger
    AFTER INSERT OR UPDATE OR DELETE ON vehicle_adatok
    FOR EACH ROW EXECUTE FUNCTION update_vehicle_search_vector_from_adatok();