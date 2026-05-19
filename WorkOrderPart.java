package ru.kafpin.autoservice.entity.operation;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WorkOrderPart {
    private Integer id;
    private Integer workOrderId;        // Внешний ключ на work_orders
    private Integer operationId;        // Внешний ключ на repair_operations (может быть null)
    private Integer partId;             // Внешний ключ на parts
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;      // Вычисляемое поле

    private LocalDateTime writtenOffAt;
    private Integer writtenOffBy;       // Внешний ключ на employees

    private Boolean isCancelled;
    private LocalDateTime cancelledAt;
    private Integer cancelledBy;        // Внешний ключ на employees

    private String notes;
}
