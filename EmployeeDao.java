package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.core.Employee;
import ru.kafpin.autoservice.enums.EmployeeRole;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EmployeeDao extends BaseDao<Employee, Integer> {
    List<Employee> findByRole(EmployeeRole role) throws SQLException;
    List<Employee> findActive() throws SQLException;
    Optional<Employee> findByLogin(String login) throws SQLException;
    List<Employee> findBySpecialization(String specialization) throws SQLException;
    List<Employee> findByQualificationLevel(Integer level) throws SQLException;
}