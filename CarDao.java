package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.core.Car;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CarDao extends BaseDao<Car, Integer> {
    List<Car> findByClientId(Integer clientId) throws SQLException;
    List<Car> findByBrandId(Integer brandId) throws SQLException;
    Optional<Car> findByVin(String vin) throws SQLException;
    Optional<Car> findByLicensePlate(String licensePlate) throws SQLException;
}