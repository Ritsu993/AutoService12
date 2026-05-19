package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.operation.RepairOperation;
import ru.kafpin.autoservice.enums.ApprovalStatus;
import ru.kafpin.autoservice.enums.OperationStatus;
import java.sql.SQLException;
import java.util.List;

public interface RepairOperationDao extends BaseDao<RepairOperation, Integer> {
    List<RepairOperation> findByWorkOrderId(Integer workOrderId) throws SQLException;
    List<RepairOperation> findByStatus(OperationStatus status) throws SQLException;
    List<RepairOperation> findByStandardWorkId(Integer standardWorkId) throws SQLException;
    List<RepairOperation> findAdditionalOperations() throws SQLException;
    List<RepairOperation> findByApprovalStatus(ApprovalStatus approvalStatus) throws SQLException;
    List<RepairOperation> findPendingApproval() throws SQLException;
    List<RepairOperation> findByCreatedBy(Integer employeeId) throws SQLException;
}