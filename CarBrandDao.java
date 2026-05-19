package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.reference.CarBrand;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CarBrandDao extends BaseDao<CarBrand, Integer> {
    Optional<CarBrand> findByBrandAndModel(String brandName, String modelName) throws SQLException;
    List<CarBrand> findByBrandName(String brandName) throws SQLException;
    List<CarBrand> findByYearRange(Integer yearFrom, Integer yearTo) throws SQLException;
}