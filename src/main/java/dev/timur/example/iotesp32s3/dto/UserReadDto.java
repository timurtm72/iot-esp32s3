package dev.timur.example.iotesp32s3.dto;

import dev.timur.example.iotesp32s3.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для представления данных пользователя без пароля
 * Используется для возврата данных клиенту
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReadDto {

    /** Уникальный идентификатор пользователя */
    private Long id;

    /** Имя пользователя */
    private String username;

    /** Электронная почта пользователя */
    private String email;

    /** Имя */
    private String firstName;

    /** Фамилия */
    private String lastName;

    /** Роль пользователя в системе */
    private Role role;

    /** Статус активности пользователя */
    private Boolean active;

    /** Время последнего входа в систему */
    private LocalDateTime lastLogin;

    /** Время создания записи */
    private LocalDateTime createdAt;

    /** Время последнего изменения записи */
    private LocalDateTime modifiedAt;
}