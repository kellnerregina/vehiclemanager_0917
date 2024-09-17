# Vehicle Manager Application

A **Vehicle Manager** egy járműkezelő rendszer, amely lehetővé teszi járművek regisztrációját, keresését, és nyilvántartását. Az alkalmazás PostgreSQL adatbázist használ, és a Spring Boot keretrendszerben készült. Az alkalmazás Docker konténerekben futtatható a mellékelt `docker-compose.yml` fájl segítségével.

## Tartalomjegyzék

1. [Jellemzők](#jellemzők)
2. [Követelmények](#követelmények)
3. [Telepítés és Futtatás](#telepítés)
    - [Környezeti változók](#környezeti-változók)
    - [Docker használata](#docker-használata)
    - [Adatbázis inicializálása](#adatbázis-inicializálása)
4. [API végpontok](#api-végpontok)
5. [Tesztelés](#tesztelés)
6. [További információk](#további-információk)

---

## Jellemzők

- Járművek regisztrációja és nyilvántartása.
- Járművek keresése rendszám, tulajdonos, és további adatok alapján.
- Keresés `tsvector` alapú indexeléssel.
- PostgreSQL adatbázis használata, SQL keresés optimalizálásával.
- Járművek hozzáadása, lekérdezése, keresése.

---

## Követelmények

Az alkalmazás futtatásához az alábbiak szükségesek:

- **Docker** és **Docker Compose** telepítve
- Java 17 vagy újabb
- Maven (opcionális, ha manuálisan futtatod)

---

## Telepítés

Az alábbi lépésekkel telepítheted és futtathatod az alkalmazást Docker konténerben.

### Környezeti változók

Az alkalmazás néhány környezeti változót használ az adatbázishoz való kapcsolódáshoz és a Spring Boot konfigurálásához. Ezek a változók a `docker-compose.yml` fájlban vannak beállítva:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/jarmuvek_db
  SPRING_DATASOURCE_USERNAME: jarmuvek_user
  SPRING_DATASOURCE_PASSWORD: Almfa_123
  SPRING_JPA_HIBERNATE_DDL_AUTO: update
  SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT: org.hibernate.dialect.PostgreSQLDialect
  ```

## Adatbázis inicializálása
Az alkalmazás a PostgreSQL adatbázist használja, amely a Docker konténer indításakor inicializálódik. A db-init mappa tartalmazza az adatbázis inicializálásához szükséges fájlokat.

## API végpontok
Az alkalmazás az alábbi REST API végpontokat biztosítja:

GET /jarmuvek: Az összes jármű darabszámát mutatja meg

GET /jarmuvek/{uuid}: Jármű lekérdezése UUID alapján

POST /jarmuvek: Új jármű hozzáadása

GET /kereses?q={kulcsszo}: Járművek keresése kulcsszó alapján

## További információk
Az alkalmazás fejlesztése során az alábbi eszközöket és technológiákat használtuk:

Java 17

Spring Boot

PostgreSQL

Hibernate (JPA)

Docker és Docker Compose


## Kapcsolat

Fejlesztő: Kellner Regina

Elérhetőség: kellner.regina@gmail.com