package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.reference.StandardWork;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StandardWorkDao extends BaseDao<StandardWork, Integer> {
    List<StandardWork> findByCategoryId(Integer categoryId) throws SQLException;
    Optional<StandardWork> findByWorkCode(String workCode) throws SQLException;
    List<StandardWork> findActive() throws SQLException;
    List<StandardWork> findByApplicableBrand(Integer brandId) throws SQLException;
    List<StandardWork> findByBaseCostRange(BigDecimal minCost, BigDecimal maxCost) throws SQLException;
    List<StandardWork> findByNameContaining(String workName) throws SQLException;
}