package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.CarBrandDao;
import ru.kafpin.autoservice.entity.reference.CarBrand;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CarBrandDaoImpl implements CarBrandDao {

    private final DatabaseConnection dbConnection;

    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    private CarBrand mapResultSet(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        return CarBrand.builder()
                .id(rs.getInt("id"))
                .brandName(rs.getString("brand_name"))
                .modelName(rs.getString("model_name"))
                .yearFrom(getIntegerOrNull(rs, "year_from"))
                .yearTo(getIntegerOrNull(rs, "year_to"))
                .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                .build();
    }

    private Integer getIntegerOrNull(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    @Override
    public Optional<CarBrand> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM car_brands WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<CarBrand> findAll() throws SQLException {
        List<CarBrand> brands = new ArrayList<>();
        String sql = "SELECT * FROM car_brands ORDER BY brand_name, model_name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                brands.add(mapResultSet(rs));
            }
        }
        return brands;
    }

    @Override
    public CarBrand save(CarBrand brand) throws SQLException {
        String sql = "INSERT INTO car_brands (brand_name, model_name, year_from, year_to) " +
                "VALUES (?, ?, ?, ?) RETURNING id, created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, brand.getBrandName());
            stmt.setString(2, brand.getModelName());
            stmt.setObject(3, brand.getYearFrom());
            stmt.setObject(4, brand.getYearTo());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp createdAt = rs.getTimestamp("created_at");
                return brand.toBuilder()
                        .id(rs.getInt("id"))
                        .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                        .build();
            }
        }
        return brand;
    }

    @Override
    public void update(CarBrand brand) throws SQLException {
        String sql = "UPDATE car_brands SET brand_name = ?, model_name = ?, year_from = ?, year_to = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, brand.getBrandName());
            stmt.setString(2, brand.getModelName());
            stmt.setObject(3, brand.getYearFrom());
            stmt.setObject(4, brand.getYearTo());
            stmt.setInt(5, brand.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM car_brands WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<CarBrand> findByBrandAndModel(String brandName, String modelName) throws SQLException {
        String sql = "SELECT * FROM car_brands WHERE brand_name = ? AND model_name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, brandName);
            stmt.setString(2, modelName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<CarBrand> findByBrandName(String brandName) throws SQLException {
        List<CarBrand> brands = new ArrayList<>();
        String sql = "SELECT * FROM car_brands WHERE brand_name = ? ORDER BY model_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, brandName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                brands.add(mapResultSet(rs));
            }
        }
        return brands;
    }

    @Override
    public List<CarBrand> findByYearRange(Integer yearFrom, Integer yearTo) throws SQLException {
        List<CarBrand> brands = new ArrayList<>();
        String sql = "SELECT * FROM car_brands WHERE year_from <= ? AND (year_to IS NULL OR year_to >= ?) ORDER BY brand_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, yearTo);
            stmt.setInt(2, yearFrom);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                brands.add(mapResultSet(rs));
            }
        }
        return brands;
    }
}