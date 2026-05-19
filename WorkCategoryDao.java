package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.reference.WorkCategory;
import java.sql.SQLException;
import java.util.Optional;

public interface WorkCategoryDao extends BaseDao<WorkCategory, Integer> {
    Optional<WorkCategory> findByCategoryName(String categoryName) throws SQLException;
}