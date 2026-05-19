package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.reference.Part;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PartDao extends BaseDao<Part, Integer> {
    List<Part> findByCategoryId(Integer categoryId) throws SQLException;
    Optional<Part> findByArticle(String article) throws SQLException;
    List<Part> findByNameContaining(String partName) throws SQLException;
    List<Part> findActive() throws SQLException;
    List<Part> findLowStock() throws SQLException;
    List<Part> findBySupplier(String supplier) throws SQLException;
    List<Part> findByCompatibleBrand(Integer brandId) throws SQLException;
    List<Part> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) throws SQLException;
}