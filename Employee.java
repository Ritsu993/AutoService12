package ru.kafpin.autoservice.entity.core;

import lombok.*;
import ru.kafpin.autoservice.enums.EmployeeRole;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String email;
    private EmployeeRole role;
    private String specialization;
    private Integer qualificationLevel;  // 1-5
    private String login;
    private String passwordHash;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
