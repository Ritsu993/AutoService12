package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.WorkOrderDao;
import ru.kafpin.autoservice.entity.operation.WorkOrder;
import ru.kafpin.autoservice.enums.WorkOrderStatus;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WorkOrderDaoImpl implements WorkOrderDao {

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

    private WorkOrder mapResultSet(ResultSet rs) throws SQLException {
        return WorkOrder.builder()
                .id(rs.getInt("id"))
                .orderNumber(rs.getString("order_number"))
                .carId(rs.getInt("car_id"))
                .clientId(rs.getInt("client_id"))
                .masterId(getIntegerOrNull(rs, "master_id"))
                .managerId(getIntegerOrNull(rs, "manager_id"))
                .status(WorkOrderStatus.valueOf(rs.getString("status").toUpperCase()))
                .description(rs.getString("description"))
                .totalNormativeHours(rs.getBigDecimal("total_normative_hours"))
                .totalActualHours(rs.getBigDecimal("total_actual_hours"))
                .totalPartsCost(rs.getBigDecimal("total_parts_cost"))
                .totalWorkCost(rs.getBigDecimal("total_work_cost"))
                .totalAmount(rs.getBigDecimal("total_amount"))
                .createdAt(getLocalDateTimeOrNull(rs, "created_at"))
                .startedAt(getLocalDateTimeOrNull(rs, "started_at"))
                .completedAt(getLocalDateTimeOrNull(rs, "completed_at"))
                .closedAt(getLocalDateTimeOrNull(rs, "closed_at"))
                .build();
    }

    @Override
    public Optional<WorkOrder> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM work_orders WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<WorkOrder> findAll() throws SQLException {
        List<WorkOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM work_orders ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(mapResultSet(rs));
            }
        }
        return orders;
    }

    @Override
    public WorkOrder save(WorkOrder order) throws SQLException {
        String sql = "INSERT INTO work_orders (order_number, car_id, client_id, master_id, manager_id, status, " +
                "description, total_normative_hours, total_actual_hours, total_parts_cost, total_work_cost, " +
                "total_amount, started_at, completed_at, closed_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            setStatementParameters(stmt, order);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return order.toBuilder()
                        .id(rs.getInt("id"))
                        .createdAt(getLocalDateTimeOrNull(rs, "created_at"))
                        .build();
            }
        }
        return order;
    }

    private void setStatementParameters(PreparedStatement stmt, WorkOrder order) throws SQLException {
        stmt.setString(1, order.getOrderNumber());
        stmt.setInt(2, order.getCarId());
        stmt.setInt(3, order.getClientId());
        stmt.setObject(4, order.getMasterId());
        stmt.setObject(5, order.getManagerId());
        stmt.setString(6, order.getStatus().getValue());
        stmt.setString(7, order.getDescription());
        stmt.setBigDecimal(8, order.getTotalNormativeHours());
        stmt.setBigDecimal(9, order.getTotalActualHours());
        stmt.setBigDecimal(10, order.getTotalPartsCost());
        stmt.setBigDecimal(11, order.getTotalWorkCost());
        stmt.setBigDecimal(12, order.getTotalAmount());
        stmt.setTimestamp(13, order.getStartedAt() != null ? Timestamp.valueOf(order.getStartedAt()) : null);
        stmt.setTimestamp(14, order.getCompletedAt() != null ? Timestamp.valueOf(order.getCompletedAt()) : null);
        stmt.setTimestamp(15, order.getClosedAt() != null ? Timestamp.valueOf(order.getClosedAt()) : null);
    }

    @Override
    public void update(WorkOrder order) throws SQLException {
        String sql = "UPDATE work_orders SET order_number = ?, car_id = ?, client_id = ?, master_id = ?, manager_id = ?, " +
                "status = ?, description = ?, total_normative_hours = ?, total_actual_hours = ?, " +
                "total_parts_cost = ?, total_work_cost = ?, total_amount = ?, started_at = ?, " +
                "completed_at = ?, closed_at = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            setStatementParameters(stmt, order);
            stmt.setInt(16, order.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM work_orders WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<WorkOrder> findByOrderNumber(String orderNumber) throws SQLException {
        String sql = "SELECT * FROM work_orders WHERE order_number = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, orderNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<WorkOrder> findByCarId(Integer carId) throws SQLException {
        return findByIntColumn("car_id", carId);
    }

    @Override
    public List<WorkOrder> findByClientId(Integer clientId) throws SQLException {
        return findByIntColumn("client_id", clientId);
    }

    @Override
    public List<WorkOrder> findByMasterId(Integer masterId) throws SQLException {
        return findByIntColumn("master_id", masterId);
    }

    @Override
    public List<WorkOrder> findByManagerId(Integer managerId) throws SQLException {
        return findByIntColumn("manager_id", managerId);
    }

    private List<WorkOrder> findByIntColumn(String column, Integer value) throws SQLException {
        List<WorkOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM work_orders WHERE " + column + " = ? ORDER BY id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, value);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSet(rs));
            }
        }
        return orders;
    }

    @Override
    public List<WorkOrder> findByStatus(WorkOrderStatus status) throws SQLException {
        List<WorkOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM work_orders WHERE status = ? ORDER BY id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, status.getValue());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSet(rs));
            }
        }
        return orders;
    }

    @Override
    public List<WorkOrder> findByDateRange(LocalDateTime from, LocalDateTime to) throws SQLException {
        List<WorkOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM work_orders WHERE created_at BETWEEN ? AND ? ORDER BY created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(from));
            stmt.setTimestamp(2, Timestamp.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSet(rs));
            }
        }
        return orders;
    }

    @Override
    public List<WorkOrder> findActive() throws SQLException {
        List<WorkOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM work_orders WHERE status NOT IN ('closed', 'cancelled') ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(mapResultSet(rs));
            }
        }
        return orders;
    }

    @Override
    public List<WorkOrder> findByTotalAmountRange(BigDecimal minAmount, BigDecimal maxAmount) throws SQLException {
        List<WorkOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM work_orders WHERE total_amount BETWEEN ? AND ? ORDER BY total_amount";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setBigDecimal(1, minAmount);
            stmt.setBigDecimal(2, maxAmount);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSet(rs));
            }
        }
        return orders;
    }
}