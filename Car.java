package ru.kafpin.autoservice.entity.core;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Car {
    private Integer id;
    private Integer clientId;        // Внешний ключ на clients
    private Integer brandId;         // Внешний ключ на car_brands
    private String vin;
    private String licensePlate;
    private Integer yearManufactured;
    private String color;
    private Integer mileage;
    private String notes;
    private LocalDateTime createdAt;
}
