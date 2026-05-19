package ru.kafpin.autoservice.entity.core;

import lombok.*;
import ru.kafpin.autoservice.enums.ClientType;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Client {
    private Integer id;
    private ClientType clientType;
    private String firstName;
    private String lastName;
    private String middleName;
    private String companyName;
    private String phone;
    private String email;
    private String address;
    private String inn;
    private LocalDateTime createdAt;
}
