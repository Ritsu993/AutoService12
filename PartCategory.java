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
public class PartCategory {
    private Integer id;
    private String categoryName;
    private String description;
    private LocalDateTime createdAt;
}