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
public class Part {
    private Integer id;
    private Integer categoryId;           // Внешний ключ на part_categories
    private String article;
    private String partName;
    private String description;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer stockQuantity;
    private Integer minStock;
    private List<Integer> compatibleBrands; // Массив как List
    private String location;
    private String supplier;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
