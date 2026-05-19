package ru.kafpin.autoservice.entity.operation;

import lombok.*;
import ru.kafpin.autoservice.enums.ApprovalStatus;
import ru.kafpin.autoservice.enums.OperationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RepairOperation {
    private Integer id;
    private Integer workOrderId;        // Внешний ключ на work_orders
    private Integer standardWorkId;     // Внешний ключ на standard_works (может быть null)
    private String operationName;
    private String description;
    private OperationStatus status;
    private BigDecimal normativeHours;
    private BigDecimal actualHours;
    private BigDecimal cost;

    // Учет времени
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime pausedAt;
    private String pauseReason;

    // Дополнительная работа
    private Boolean isAdditional;
    private ApprovalStatus approvalStatus;
    private Integer approvedBy;         // Внешний ключ на employees
    private LocalDateTime approvedAt;
    private String rejectionReason;

    // Комментарии
    private String masterComments;
    private String deviationReason;

    private LocalDateTime createdAt;
    private Integer createdBy;          // Внешний ключ на employees
}
