package ru.kafpin.autoservice.entity.reference;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CarBrand {
    private Integer id;
    private String brandName;
    private String modelName;
    private Integer yearFrom;
    private Integer yearTo;
    private LocalDateTime createdAt;
}
