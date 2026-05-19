package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.PartDao;
import ru.kafpin.autoservice.entity.reference.Part;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PartDaoImpl implements PartDao {

    private final DatabaseConnection dbConnection;

    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    @SuppressWarnings("unchecked")
    private Part mapResultSet(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Array compatibleBrandsArray = rs.getArray("compatible_brands");
        List<Integer> compatibleBrands = null;
        if (compatibleBrandsArray != null) {
            compatibleBrands = Arrays.asList((Integer[]) compatibleBrandsArray.getArray());
        }

        return Part.builder()
                .id(rs.getInt("id"))
                .categoryId(getIntegerOrNull(rs, "category_id"))
                .article(rs.getString("article"))
                .partName(rs.getString("part_name"))
                .description(rs.getString("description"))
                .purchasePrice(rs.getBigDecimal("purchase_price"))
                .salePrice(rs.getBigDecimal("sale_price"))
                .stockQuantity(rs.getInt("stock_quantity"))
                .minStock(getIntegerOrNull(rs, "min_stock"))
                .compatibleBrands(compatibleBrands)
                .location(rs.getString("location"))
                .supplier(rs.getString("supplier"))
                .isActive(rs.getBoolean("is_active"))
                .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                .build();
    }

    private Integer getIntegerOrNull(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private void setCompatibleBrands(PreparedStatement stmt, int parameterIndex, List<Integer> brands) throws SQLException {
        if (brands != null && !brands.isEmpty()) {
            Integer[] brandsArray = brands.toArray(new Integer[0]);
            stmt.setArray(parameterIndex, getConnection().createArrayOf("integer", brandsArray));
        } else {
            stmt.setNull(parameterIndex, Types.ARRAY);
        }
    }

    @Override
    public Optional<Part> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM parts WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Part> findAll() throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts ORDER BY part_name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public Part save(Part part) throws SQLException {
        String sql = "INSERT INTO parts (category_id, article, part_name, description, purchase_price, sale_price, " +
                "stock_quantity, min_stock, compatible_brands, location, supplier, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, part.getCategoryId());
            stmt.setString(2, part.getArticle());
            stmt.setString(3, part.getPartName());
            stmt.setString(4, part.getDescription());
            stmt.setBigDecimal(5, part.getPurchasePrice());
            stmt.setBigDecimal(6, part.getSalePrice());
            stmt.setInt(7, part.getStockQuantity() != null ? part.getStockQuantity() : 0);
            stmt.setObject(8, part.getMinStock());
            setCompatibleBrands(stmt, 9, part.getCompatibleBrands());
            stmt.setString(10, part.getLocation());
            stmt.setString(11, part.getSupplier());
            stmt.setBoolean(12, Boolean.TRUE.equals(part.getIsActive()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp createdAt = rs.getTimestamp("created_at");
                return part.toBuilder()
                        .id(rs.getInt("id"))
                        .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                        .build();
            }
        }
        return part;
    }

    @Override
    public void update(Part part) throws SQLException {
        String sql = "UPDATE parts SET category_id = ?, article = ?, part_name = ?, description = ?, " +
                "purchase_price = ?, sale_price = ?, stock_quantity = ?, min_stock = ?, " +
                "compatible_brands = ?, location = ?, supplier = ?, is_active = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setObject(1, part.getCategoryId());
            stmt.setString(2, part.getArticle());
            stmt.setString(3, part.getPartName());
            stmt.setString(4, part.getDescription());
            stmt.setBigDecimal(5, part.getPurchasePrice());
            stmt.setBigDecimal(6, part.getSalePrice());
            stmt.setInt(7, part.getStockQuantity());
            stmt.setObject(8, part.getMinStock());
            setCompatibleBrands(stmt, 9, part.getCompatibleBrands());
            stmt.setString(10, part.getLocation());
            stmt.setString(11, part.getSupplier());
            stmt.setBoolean(12, Boolean.TRUE.equals(part.getIsActive()));
            stmt.setInt(13, part.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM parts WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Part> findByCategoryId(Integer categoryId) throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts WHERE category_id = ? ORDER BY part_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public Optional<Part> findByArticle(String article) throws SQLException {
        String sql = "SELECT * FROM parts WHERE article = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, article);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Part> findByNameContaining(String partName) throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts WHERE part_name ILIKE ? ORDER BY part_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + partName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public List<Part> findActive() throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts WHERE is_active = true ORDER BY part_name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public List<Part> findLowStock() throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts WHERE stock_quantity <= COALESCE(min_stock, 10) ORDER BY stock_quantity";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public List<Part> findBySupplier(String supplier) throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts WHERE supplier ILIKE ? ORDER BY part_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + supplier + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public List<Part> findByCompatibleBrand(Integer brandId) throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts WHERE ? = ANY(compatible_brands) ORDER BY part_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public List<Part> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts WHERE sale_price BETWEEN ? AND ? ORDER BY sale_price";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setBigDecimal(1, minPrice);
            stmt.setBigDecimal(2, maxPrice);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }
}