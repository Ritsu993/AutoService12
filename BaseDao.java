package ru.kafpin.autoservice.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseDao<T, ID> {
    Optional<T> findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
    T save(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void deleteById(ID id) throws SQLException;
}