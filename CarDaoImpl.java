package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.CarDao;
import ru.kafpin.autoservice.entity.core.Car;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CarDaoImpl implements CarDao {

    private final DatabaseConnection dbConnection;

    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    private Integer getIntegerOrNull(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private LocalDateTime getLocalDateTimeOrNull(ResultSet rs, String column) throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private Car mapResultSet(ResultSet rs) throws SQLException {
        return Car.builder()
                .id(rs.getInt("id"))
                .clientId(rs.getInt("client_id"))
                .brandId(rs.getInt("brand_id"))
                .vin(rs.getString("vin"))
                .licensePlate(rs.getString("license_plate"))
                .yearManufactured(getIntegerOrNull(rs, "year_manufactured"))
                .color(rs.getString("color"))
                .mileage(getIntegerOrNull(rs, "mileage"))
                .notes(rs.getString("notes"))
                .createdAt(getLocalDateTimeOrNull(rs, "created_at"))
                .build();
    }

    @Override
    public Optional<Car> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM cars WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Car> findAll() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cars.add(mapResultSet(rs));
            }
        }
        return cars;
    }

    @Override
    public Car save(Car car) throws SQLException {
        String sql = "INSERT INTO cars (client_id, brand_id, vin, license_plate, year_manufactured, color, mileage, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, car.getClientId());
            stmt.setInt(2, car.getBrandId());
            stmt.setString(3, car.getVin());
            stmt.setString(4, car.getLicensePlate());
            stmt.setObject(5, car.getYearManufactured());
            stmt.setString(6, car.getColor());
            stmt.setObject(7, car.getMileage());
            stmt.setString(8, car.getNotes());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // ✅ toBuilder() теперь работает!
                return car.toBuilder()
                        .id(rs.getInt("id"))
                        .createdAt(getLocalDateTimeOrNull(rs, "created_at"))
                        .build();
            }
        }
        return car;
    }

    @Override
    public void update(Car car) throws SQLException {
        String sql = "UPDATE cars SET client_id = ?, brand_id = ?, vin = ?, license_plate = ?, " +
                "year_manufactured = ?, color = ?, mileage = ?, notes = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, car.getClientId());
            stmt.setInt(2, car.getBrandId());
            stmt.setString(3, car.getVin());
            stmt.setString(4, car.getLicensePlate());
            stmt.setObject(5, car.getYearManufactured());
            stmt.setString(6, car.getColor());
            stmt.setObject(7, car.getMileage());
            stmt.setString(8, car.getNotes());
            stmt.setInt(9, car.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM cars WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Car> findByClientId(Integer clientId) throws SQLException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars WHERE client_id = ? ORDER BY id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cars.add(mapResultSet(rs));
            }
        }
        return cars;
    }

    @Override
    public List<Car> findByBrandId(Integer brandId) throws SQLException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars WHERE brand_id = ? ORDER BY id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cars.add(mapResultSet(rs));
            }
        }
        return cars;
    }

    @Override
    public Optional<Car> findByVin(String vin) throws SQLException {
        String sql = "SELECT * FROM cars WHERE vin = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, vin);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public Optional<Car> findByLicensePlate(String licensePlate) throws SQLException {
        String sql = "SELECT * FROM cars WHERE license_plate = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }
}