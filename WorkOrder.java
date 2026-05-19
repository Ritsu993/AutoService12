package ru.kafpin.autoservice.entity.operation;

import lombok.*;
import ru.kafpin.autoservice.enums.WorkOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WorkOrder {
    private Integer id;
    private String orderNumber;
    private Integer carId;              // Внешний ключ на cars
    private Integer clientId;           // Внешний ключ на clients
    private Integer masterId;           // Внешний ключ на employees (мастер)
    private Integer managerId;          // Внешний ключ на employees (менеджер)
    private WorkOrderStatus status;
    private String description;
    private BigDecimal totalNormativeHours;
    private BigDecimal totalActualHours;
    private BigDecimal totalPartsCost;
    private BigDecimal totalWorkCost;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime closedAt;
}