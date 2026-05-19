package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.PartCategoryDao;
import ru.kafpin.autoservice.entity.reference.PartCategory;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PartCategoryDaoImpl implements PartCategoryDao {

    private final DatabaseConnection dbConnection;

    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    private PartCategory mapResultSet(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        return PartCategory.builder()
                .id(rs.getInt("id"))
                .categoryName(rs.getString("category_name"))
                .description(rs.getString("description"))
                .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                .build();
    }

    @Override
    public Optional<PartCategory> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM part_categories WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<PartCategory> findAll() throws SQLException {
        List<PartCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM part_categories ORDER BY category_name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(mapResultSet(rs));
            }
        }
        return categories;
    }

    @Override
    public PartCategory save(PartCategory category) throws SQLException {
        String sql = "INSERT INTO part_categories (category_name, description) " +
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
    public void update(PartCategory category) throws SQLException {
        String sql = "UPDATE part_categories SET category_name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM part_categories WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<PartCategory> findByCategoryName(String categoryName) throws SQLException {
        String sql = "SELECT * FROM part_categories WHERE category_name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }
}