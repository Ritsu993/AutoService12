package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.WorkCategoryDao;
import ru.kafpin.autoservice.entity.reference.WorkCategory;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WorkCategoryDaoImpl implements WorkCategoryDao {

    private final DatabaseConnection dbConnection;

    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    private WorkCategory mapResultSet(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        return WorkCategory.builder()
                .id(rs.getInt("id"))
                .categoryName(rs.getString("category_name"))
                .description(rs.getString("description"))
                .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                .build();
    }

    @Override
    public Optional<WorkCategory> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM work_categories WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<WorkCategory> findAll() throws SQLException {
        List<WorkCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM work_categories ORDER BY category_name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(mapResultSet(rs));
            }
        }
        return categories;
    }

    @Override
    public WorkCategory save(WorkCategory category) throws SQLException {
        String sql = "INSERT INTO work_categories (category_name, description) " +
                "VALUES (?, ?) RETURNING id, created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp createdAt = rs.getTimestamp("created_at");
                return category.toBuilder()
                        .id(rs.getInt("id"))
                        .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                        .build();
            }
        }
        return category;
    }

    @Override
    public void update(WorkCategory category) throws SQLException {
        String sql = "UPDATE work_categories SET category_name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM work_categories WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<WorkCategory> findByCategoryName(String categoryName) throws SQLException {
        String sql = "SELECT * FROM work_categories WHERE category_name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }
}