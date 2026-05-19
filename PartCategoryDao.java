package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.reference.PartCategory;
import java.sql.SQLException;
import java.util.Optional;

public interface PartCategoryDao extends BaseDao<PartCategory, Integer> {
    Optional<PartCategory> findByCategoryName(String categoryName) throws SQLException;
}