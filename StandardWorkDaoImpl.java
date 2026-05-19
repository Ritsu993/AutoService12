package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.StandardWorkDao;
import ru.kafpin.autoservice.entity.reference.StandardWork;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class StandardWorkDaoImpl implements StandardWorkDao {

    private final DatabaseConnection dbConnection;

    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    @SuppressWarnings("unchecked")
    private StandardWork mapResultSet(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Array applicableBrandsArray = rs.getArray("applicable_brands");
        List<Integer> applicableBrands = null;
        if (applicableBrandsArray != null) {
            applicableBrands = Arrays.asList((Integer[]) applicableBrandsArray.getArray());
        }

        return StandardWork.builder()
                .id(rs.getInt("id"))
                .categoryId(getIntegerOrNull(rs, "category_id"))
                .workCode(rs.getString("work_code"))
                .workName(rs.getString("work_name"))
                .description(rs.getString("description"))
                .normativeHours(rs.getBigDecimal("normative_hours"))
                .baseCost(rs.getBigDecimal("base_cost"))
                .requiredParts(rs.getString("required_parts"))
                .applicableBrands(applicableBrands)
                .isActive(rs.getBoolean("is_active"))
                .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                .build();
    }

    private Integer getIntegerOrNull(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private void setApplicableBrands(PreparedStatement stmt, int parameterIndex, List<Integer> brands) throws SQLException {
        if (brands != null && !brands.isEmpty()) {
            Integer[] brandsArray = brands.toArray(new Integer[0]);
            stmt.setArray(parameterIndex, getConnection().createArrayOf("integer", brandsArray));
        } else {
            stmt.setNull(parameterIndex, Types.ARRAY);
        }
    }

    @Override
    public Optional<StandardWork> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM standard_works WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<StandardWork> findAll() throws SQLException {
        List<StandardWork> works = new ArrayList<>();
        String sql = "SELECT * FROM standard_works ORDER BY work_name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                works.add(mapResultSet(rs));
            }
        }
        return works;
    }

    @Override
    public StandardWork save(StandardWork work) throws SQLException {
        String sql = "INSERT INTO standard_works (category_id, work_code, work_name, description, normative_hours, " +
                "base_cost, required_parts, applicable_brands, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, work.getCategoryId());
            stmt.setString(2, work.getWorkCode());
            stmt.setString(3, work.getWorkName());
            stmt.setString(4, work.getDescription());
            stmt.setBigDecimal(5, work.getNormativeHours());
            stmt.setBigDecimal(6, work.getBaseCost());
            stmt.setString(7, work.getRequiredParts());
            setApplicableBrands(stmt, 8, work.getApplicableBrands());
            stmt.setBoolean(9, Boolean.TRUE.equals(work.getIsActive()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp createdAt = rs.getTimestamp("created_at");
                return work.toBuilder()
                        .id(rs.getInt("id"))
                        .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                        .build();
            }
        }
        return work;
    }

    @Override
    public void update(StandardWork work) throws SQLException {
        String sql = "UPDATE standard_works SET category_id = ?, work_code = ?, work_name = ?, description = ?, " +
                "normative_hours = ?, base_cost = ?, required_parts = ?, applicable_brands = ?, is_active = ? " +
                "WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, work.getCategoryId());
            stmt.setString(2, work.getWorkCode());
            stmt.setString(3, work.getWorkName());
            stmt.setString(4, work.getDescription());
            stmt.setBigDecimal(5, work.getNormativeHours());
            stmt.setBigDecimal(6, work.getBaseCost());
            stmt.setString(7, work.getRequiredParts());
            setApplicableBrands(stmt, 8, work.getApplicableBrands());
            stmt.setBoolean(9, Boolean.TRUE.equals(work.getIsActive()));
            stmt.setInt(10, work.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM standard_works WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<StandardWork> findByCategoryId(Integer categoryId) throws SQLException {
        List<StandardWork> works = new ArrayList<>();
        String sql = "SELECT * FROM standard_works WHERE category_id = ? ORDER BY work_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                works.add(mapResultSet(rs));
            }
        }
        return works;
    }

    @Override
    public Optional<StandardWork> findByWorkCode(String workCode) throws SQLException {
        String sql = "SELECT * FROM standard_works WHERE work_code = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, workCode);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<StandardWork> findActive() throws SQLException {
        List<StandardWork> works = new ArrayList<>();
        String sql = "SELECT * FROM standard_works WHERE is_active = true ORDER BY work_name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                works.add(mapResultSet(rs));
            }
        }
        return works;
    }

    @Override
    public List<StandardWork> findByApplicableBrand(Integer brandId) throws SQLException {
        List<StandardWork> works = new ArrayList<>();
        String sql = "SELECT * FROM standard_works WHERE ? = ANY(applicable_brands) OR applicable_brands IS NULL ORDER BY work_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                works.add(mapResultSet(rs));
            }
        }
        return works;
    }

    @Override
    public List<StandardWork> findByBaseCostRange(BigDecimal minCost, BigDecimal maxCost) throws SQLException {
        List<StandardWork> works = new ArrayList<>();
        String sql = "SELECT * FROM standard_works WHERE base_cost BETWEEN ? AND ? ORDER BY base_cost";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setBigDecimal(1, minCost);
            stmt.setBigDecimal(2, maxCost);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                works.add(mapResultSet(rs));
            }
        }
        return works;
    }

    @Override
    public List<StandardWork> findByNameContaining(String workName) throws SQLException {
        List<StandardWork> works = new ArrayList<>();
        String sql = "SELECT * FROM standard_works WHERE work_name ILIKE ? ORDER BY work_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + workName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                works.add(mapResultSet(rs));
            }
        }
        return works;
    }
}