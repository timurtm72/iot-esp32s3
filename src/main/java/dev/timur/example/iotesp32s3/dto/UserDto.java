package dev.timur.example.iotesp32s3.dto;

import dev.timur.example.iotesp32s3.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO для передачи данных пользователя в системе IoT.
 * Используется для операций создания и изменения пользователей.
 * 
 * ВНИМАНИЕ: Включает пароль! Используйте осторожно и только там, где необходимо.
 * Для операций чтения используйте UserReadDto без пароля.
 * 
 * Особенности:
 * - Поддерживает все поля пользователя включая конфиденциальные данные
 * - Использует Lombok для автоматической генерации геттеров/сеттеров
 * - Поддерживает Builder паттерн для удобного создания объектов
 * - Включает временные метки для аудита изменений
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    
    /** Уникальный идентификатор пользователя */
    private Long id;
    
    /** Уникальное имя пользователя для входа в систему */
    private String username;
    
    /** Уникальный email адрес пользователя */
    private String email;
    
    /** Пароль пользователя (КОНФИДЕНЦИАЛЬНО - использовать осторожно!) */
    private String password;
    
    /** Имя пользователя */
    private String firstName;
    
    /** Фамилия пользователя */
    private String lastName;
    
    /** Роль пользователя в системе (ADMIN, USER, etc.) */
    private Role role;
    
    /** Статус активности пользователя (true - активен, false - заблокирован) */
    private Boolean active;
    
    /** Дата и время создания пользователя */
    private LocalDateTime createdAt;
    
    /** Дата и время последнего изменения данных пользователя */
    private LocalDateTime modifiedAt;
    
    /** Дата и время последнего входа в систему */
    private LocalDateTime lastLogin;
    // Пароль не включаем в DTO для безопасности
} 