package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.EmployeeDao;
import ru.kafpin.autoservice.entity.core.Employee;
import ru.kafpin.autoservice.enums.EmployeeRole;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeDaoImpl implements EmployeeDao {

    private final DatabaseConnection dbConnection;

    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    private Employee mapResultSet(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        int qualificationLevel = rs.getInt("qualification_level");
        return Employee.builder()
                .id(rs.getInt("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .middleName(rs.getString("middle_name"))
                .phone(rs.getString("phone"))
                .email(rs.getString("email"))
                .role(EmployeeRole.valueOf(rs.getString("role").toUpperCase()))
                .specialization(rs.getString("specialization"))
                .qualificationLevel(rs.wasNull() ? null : qualificationLevel)
                .login(rs.getString("login"))
                .passwordHash(rs.getString("password_hash"))
                .isActive(rs.getBoolean("is_active"))
                .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                .build();
    }

    @Override
    public Optional<Employee> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY last_name, first_name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapResultSet(rs));
            }
        }
        return employees;
    }

    @Override
    public Employee save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (first_name, last_name, middle_name, phone, email, role, specialization, " +
                "qualification_level, login, password_hash, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getMiddleName());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getEmail());
            stmt.setString(6, employee.getRole().getValue());
            stmt.setString(7, employee.getSpecialization());
            stmt.setObject(8, employee.getQualificationLevel());
            stmt.setString(9, employee.getLogin());
            stmt.setString(10, employee.getPasswordHash());
            stmt.setBoolean(11, Boolean.TRUE.equals(employee.getIsActive()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp createdAt = rs.getTimestamp("created_at");
                return employee.toBuilder()
                        .id(rs.getInt("id"))
                        .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                        .build();
            }
        }
        return employee;
    }

    @Override
    public void update(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, middle_name = ?, phone = ?, email = ?, " +
                "role = ?, specialization = ?, qualification_level = ?, login = ?, password_hash = ?, is_active = ? " +
                "WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getMiddleName());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getEmail());
            stmt.setString(6, employee.getRole().getValue());
            stmt.setString(7, employee.getSpecialization());
            stmt.setObject(8, employee.getQualificationLevel());
            stmt.setString(9, employee.getLogin());
            stmt.setString(10, employee.getPasswordHash());
            stmt.setBoolean(11, Boolean.TRUE.equals(employee.getIsActive()));
            stmt.setInt(12, employee.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Employee> findByRole(EmployeeRole role) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE role = ? ORDER BY last_name, first_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, role.getValue());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employees.add(mapResultSet(rs));
            }
        }
        return employees;
    }

    @Override
    public List<Employee> findActive() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE is_active = true ORDER BY last_name, first_name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapResultSet(rs));
            }
        }
        return employees;
    }

    @Override
    public Optional<Employee> findByLogin(String login) throws SQLException {
        String sql = "SELECT * FROM employees WHERE login = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Employee> findBySpecialization(String specialization) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE specialization ILIKE ? ORDER BY last_name, first_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + specialization + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employees.add(mapResultSet(rs));
            }
        }
        return employees;
    }

    @Override
    public List<Employee> findByQualificationLevel(Integer level) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE qualification_level = ? ORDER BY last_name, first_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, level);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employees.add(mapResultSet(rs));
            }
        }
        return employees;
    }
}