package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.RepairOperationDao;
import ru.kafpin.autoservice.entity.operation.RepairOperation;
import ru.kafpin.autoservice.enums.ApprovalStatus;
import ru.kafpin.autoservice.enums.OperationStatus;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RepairOperationDaoImpl implements RepairOperationDao {

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

    private ApprovalStatus getApprovalStatusOrNull(ResultSet rs, String column) throws SQLException {
        String value = rs.getString(column);
        return value != null ? ApprovalStatus.valueOf(value.toUpperCase()) : null;
    }

    private RepairOperation mapResultSet(ResultSet rs) throws SQLException {
        return RepairOperation.builder()
                .id(rs.getInt("id"))
                .workOrderId(rs.getInt("work_order_id"))
                .standardWorkId(getIntegerOrNull(rs, "standard_work_id"))
                .operationName(rs.getString("operation_name"))
                .description(rs.getString("description"))
                .status(OperationStatus.valueOf(rs.getString("status").toUpperCase()))
                .normativeHours(rs.getBigDecimal("normative_hours"))
                .actualHours(rs.getBigDecimal("actual_hours"))
                .cost(rs.getBigDecimal("cost"))
                .startedAt(getLocalDateTimeOrNull(rs, "started_at"))
                .completedAt(getLocalDateTimeOrNull(rs, "completed_at"))
                .pausedAt(getLocalDateTimeOrNull(rs, "paused_at"))
                .pauseReason(rs.getString("pause_reason"))
                .isAdditional(rs.getBoolean("is_additional"))
                .approvalStatus(getApprovalStatusOrNull(rs, "approval_status"))
                .approvedBy(getIntegerOrNull(rs, "approved_by"))
                .approvedAt(getLocalDateTimeOrNull(rs, "approved_at"))
                .rejectionReason(rs.getString("rejection_reason"))
                .masterComments(rs.getString("master_comments"))
                .deviationReason(rs.getString("deviation_reason"))
                .createdAt(getLocalDateTimeOrNull(rs, "created_at"))
                .createdBy(getIntegerOrNull(rs, "created_by"))
                .build();
    }

    @Override
    public Optional<RepairOperation> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM repair_operations WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<RepairOperation> findAll() throws SQLException {
        List<RepairOperation> operations = new ArrayList<>();
        String sql = "SELECT * FROM repair_operations ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                operations.add(mapResultSet(rs));
            }
        }
        return operations;
    }

    @Override
    public RepairOperation save(RepairOperation operation) throws SQLException {
        String sql = "INSERT INTO repair_operations (work_order_id, standard_work_id, operation_name, description, " +
                "status, normative_hours, actual_hours, cost, started_at, completed_at, paused_at, pause_reason, " +
                "is_additional, approval_status, approved_by, approved_at, rejection_reason, master_comments, " +
                "deviation_reason, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            setStatementParameters(stmt, operation);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return operation.toBuilder()
                        .id(rs.getInt("id"))
                        .createdAt(getLocalDateTimeOrNull(rs, "created_at"))
                        .build();
            }
        }
        return operation;
    }

    private void setStatementParameters(PreparedStatement stmt, RepairOperation op) throws SQLException {
        stmt.setInt(1, op.getWorkOrderId());
        stmt.setObject(2, op.getStandardWorkId());
        stmt.setString(3, op.getOperationName());
        stmt.setString(4, op.getDescription());
        stmt.setString(5, op.getStatus().getValue());
        stmt.setBigDecimal(6, op.getNormativeHours());
        stmt.setBigDecimal(7, op.getActualHours());
        stmt.setBigDecimal(8, op.getCost());
        stmt.setTimestamp(9, op.getStartedAt() != null ? Timestamp.valueOf(op.getStartedAt()) : null);
        stmt.setTimestamp(10, op.getCompletedAt() != null ? Timestamp.valueOf(op.getCompletedAt()) : null);
        stmt.setTimestamp(11, op.getPausedAt() != null ? Timestamp.valueOf(op.getPausedAt()) : null);
        stmt.setString(12, op.getPauseReason());
        stmt.setBoolean(13, Boolean.TRUE.equals(op.getIsAdditional()));
        stmt.setString(14, op.getApprovalStatus() != null ? op.getApprovalStatus().getValue() : null);
        stmt.setObject(15, op.getApprovedBy());
        stmt.setTimestamp(16, op.getApprovedAt() != null ? Timestamp.valueOf(op.getApprovedAt()) : null);
        stmt.setString(17, op.getRejectionReason());
        stmt.setString(18, op.getMasterComments());
        stmt.setString(19, op.getDeviationReason());
        stmt.setObject(20, op.getCreatedBy());
    }

    @Override
    public void update(RepairOperation operation) throws SQLException {
        String sql = "UPDATE repair_operations SET work_order_id = ?, standard_work_id = ?, operation_name = ?, " +
                "description = ?, status = ?, normative_hours = ?, actual_hours = ?, cost = ?, started_at = ?, " +
                "completed_at = ?, paused_at = ?, pause_reason = ?, is_additional = ?, approval_status = ?, " +
                "approved_by = ?, approved_at = ?, rejection_reason = ?, master_comments = ?, deviation_reason = ?, " +
                "created_by = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            setStatementParameters(stmt, operation);
            stmt.setInt(21, operation.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM repair_operations WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<RepairOperation> findByWorkOrderId(Integer workOrderId) throws SQLException {
        return findByIntColumn("work_order_id", workOrderId);
    }

    @Override
    public List<RepairOperation> findByStatus(OperationStatus status) throws SQLException {
        List<RepairOperation> operations = new ArrayList<>();
        String sql = "SELECT * FROM repair_operations WHERE status = ? ORDER BY id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, status.getValue());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                operations.add(mapResultSet(rs));
            }
        }
        return operations;
    }

    @Override
    public List<RepairOperation> findByStandardWorkId(Integer standardWorkId) throws SQLException {
        return findByIntColumn("standard_work_id", standardWorkId);
    }

    private List<RepairOperation> findByIntColumn(String column, Integer value) throws SQLException {
        List<RepairOperation> operations = new ArrayList<>();
        String sql = "SELECT * FROM repair_operations WHERE " + column + " = ? ORDER BY id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, value);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                operations.add(mapResultSet(rs));
            }
        }
        return operations;
    }

    @Override
    public List<RepairOperation> findAdditionalOperations() throws SQLException {
        List<RepairOperation> operations = new ArrayList<>();
        String sql = "SELECT * FROM repair_operations WHERE is_additional = true ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                operations.add(mapResultSet(rs));
            }
        }
        return operations;
    }

    @Override
    public List<RepairOperation> findByApprovalStatus(ApprovalStatus approvalStatus) throws SQLException {
        List<RepairOperation> operations = new ArrayList<>();
        String sql = "SELECT * FROM repair_operations WHERE approval_status = ? ORDER BY id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, approvalStatus.getValue());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                operations.add(mapResultSet(rs));
            }
        }
        return operations;
    }

    @Override
    public List<RepairOperation> findPendingApproval() throws SQLException {
        List<RepairOperation> operations = new ArrayList<>();
        String sql = "SELECT * FROM repair_operations WHERE approval_status = 'pending' OR " +
                "(is_additional = true AND approval_status IS NULL) ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                operations.add(mapResultSet(rs));
            }
        }
        return operations;
    }

    @Override
    public List<RepairOperation> findByCreatedBy(Integer employeeId) throws SQLException {
        return findByIntColumn("created_by", employeeId);
    }
}