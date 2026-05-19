package ru.kafpin.autoservice.entity.reference;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StandardWork {
    private Integer id;
    private Integer categoryId;           // Внешний ключ на work_categories
    private String workCode;
    private String workName;
    private String description;
    private BigDecimal normativeHours;
    private BigDecimal baseCost;
    private String requiredParts;         // JSON как строка
    private List<Integer> applicableBrands; // Массив как List
    private Boolean isActive;
    private LocalDateTime createdAt;
}
