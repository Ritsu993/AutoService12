package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.operation.WorkOrder;
import ru.kafpin.autoservice.enums.WorkOrderStatus;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkOrderDao extends BaseDao<WorkOrder, Integer> {
    Optional<WorkOrder> findByOrderNumber(String orderNumber) throws SQLException;
    List<WorkOrder> findByCarId(Integer carId) throws SQLException;
    List<WorkOrder> findByClientId(Integer clientId) throws SQLException;
    List<WorkOrder> findByMasterId(Integer masterId) throws SQLException;
    List<WorkOrder> findByManagerId(Integer managerId) throws SQLException;
    List<WorkOrder> findByStatus(WorkOrderStatus status) throws SQLException;
    List<WorkOrder> findByDateRange(LocalDateTime from, LocalDateTime to) throws SQLException;
    List<WorkOrder> findActive() throws SQLException;
    List<WorkOrder> findByTotalAmountRange(BigDecimal minAmount, BigDecimal maxAmount) throws SQLException;
}