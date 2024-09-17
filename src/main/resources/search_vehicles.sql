-- search_vehicles.sql
SELECT v.*
FROM vehicle v
         LEFT JOIN vehicle_adatok va ON v.uuid = va.vehicle_uuid
WHERE v.rendszam LIKE CONCAT('%', :q, '%')
   OR v.tulajdonos LIKE CONCAT('%', :q, '%')
   OR va.adatok LIKE CONCAT('%', :q, '%');

``