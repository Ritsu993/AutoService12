package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.operation.WorkOrderPart;
import java.sql.SQLException;
import java.util.List;

public interface WorkOrderPartDao extends BaseDao<WorkOrderPart, Integer> {
    List<WorkOrderPart> findByWorkOrderId(Integer workOrderId) throws SQLException;
    List<WorkOrderPart> findByOperationId(Integer operationId) throws SQLException;
    List<WorkOrderPart> findByPartId(Integer partId) throws SQLException;
    List<WorkOrderPart> findActive() throws SQLException;
    List<WorkOrderPart> findCancelled() throws SQLException;
}