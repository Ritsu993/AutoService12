package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.WorkOrderPartDao;
import ru.kafpin.autoservice.entity.operation.WorkOrderPart;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WorkOrderPartDaoImpl implements WorkOrderPartDao {

    private final DatabaseConnection dbConnection;

    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    private LocalDateTime getLocalDateTimeOrNull(ResultSet rs, String column) throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private Integer getIntegerOrNull(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private WorkOrderPart mapResultSet(ResultSet rs) throws SQLException {
        return WorkOrderPart.builder()
                .id(rs.getInt("id"))
                .workOrderId(rs.getInt("work_order_id"))
                .operationId(getIntegerOrNull(rs, "operation_id"))
                .partId(rs.getInt("part_id"))
                .quantity(rs.getInt("quantity"))
                .unitPrice(rs.getBigDecimal("unit_price"))
                .totalPrice(rs.getBigDecimal("total_price"))
                .writtenOffAt(getLocalDateTimeOrNull(rs, "written_off_at"))
                .writtenOffBy(getIntegerOrNull(rs, "written_off_by"))
                .isCancelled(rs.getBoolean("is_cancelled"))
                .cancelledAt(getLocalDateTimeOrNull(rs, "cancelled_at"))
                .cancelledBy(getIntegerOrNull(rs, "cancelled_by"))
                .notes(rs.getString("notes"))
                .build();
    }

    @Override
    public Optional<WorkOrderPart> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM work_order_parts WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<WorkOrderPart> findAll() throws SQLException {
        List<WorkOrderPart> parts = new ArrayList<>();
        String sql = "SELECT * FROM work_order_parts ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public WorkOrderPart save(WorkOrderPart part) throws SQLException {
        String sql = "INSERT INTO work_order_parts (work_order_id, operation_id, part_id, quantity, unit_price, " +
                "total_price, written_off_at, written_off_by, is_cancelled, cancelled_at, cancelled_by, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            setStatementParameters(stmt, part);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return part.toBuilder()
                        .id(rs.getInt("id"))
                        .build();
            }
        }
        return part;
    }

    private void setStatementParameters(PreparedStatement stmt, WorkOrderPart part) throws SQLException {
        stmt.setInt(1, part.getWorkOrderId());
        stmt.setObject(2, part.getOperationId());
        stmt.setInt(3, part.getPartId());
        stmt.setInt(4, part.getQuantity());
        stmt.setBigDecimal(5, part.getUnitPrice());
        stmt.setBigDecimal(6, part.getTotalPrice());
        stmt.setTimestamp(7, part.getWrittenOffAt() != null ? Timestamp.valueOf(part.getWrittenOffAt()) : null);
        stmt.setObject(8, part.getWrittenOffBy());
        stmt.setBoolean(9, Boolean.TRUE.equals(part.getIsCancelled()));
        stmt.setTimestamp(10, part.getCancelledAt() != null ? Timestamp.valueOf(part.getCancelledAt()) : null);
        stmt.setObject(11, part.getCancelledBy());
        stmt.setString(12, part.getNotes());
    }

    @Override
    public void update(WorkOrderPart part) throws SQLException {
        String sql = "UPDATE work_order_parts SET work_order_id = ?, operation_id = ?, part_id = ?, quantity = ?, " +
                "unit_price = ?, total_price = ?, written_off_at = ?, written_off_by = ?, is_cancelled = ?, " +
                "cancelled_at = ?, cancelled_by = ?, notes = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            setStatementParameters(stmt, part);
            stmt.setInt(13, part.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM work_order_parts WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<WorkOrderPart> findByWorkOrderId(Integer workOrderId) throws SQLException {
        return findByIntColumn("work_order_id", workOrderId);
    }

    @Override
    public List<WorkOrderPart> findByOperationId(Integer operationId) throws SQLException {
        return findByIntColumn("operation_id", operationId);
    }

    @Override
    public List<WorkOrderPart> findByPartId(Integer partId) throws SQLException {
        return findByIntColumn("part_id", partId);
    }

    private List<WorkOrderPart> findByIntColumn(String column, Integer value) throws SQLException {
        List<WorkOrderPart> parts = new ArrayList<>();
        String sql = "SELECT * FROM work_order_parts WHERE " + column + " = ? ORDER BY id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, value);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public List<WorkOrderPart> findActive() throws SQLException {
        List<WorkOrderPart> parts = new ArrayList<>();
        String sql = "SELECT * FROM work_order_parts WHERE is_cancelled = false ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }

    @Override
    public List<WorkOrderPart> findCancelled() throws SQLException {
        List<WorkOrderPart> parts = new ArrayList<>();
        String sql = "SELECT * FROM work_order_parts WHERE is_cancelled = true ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                parts.add(mapResultSet(rs));
            }
        }
        return parts;
    }
}