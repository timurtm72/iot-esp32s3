package dev.timur.example.iotesp32s3.dto;

import dev.timur.example.iotesp32s3.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Безопасный DTO для чтения данных пользователя в системе IoT.
 * Исключает конфиденциальные данные (пароль) для безопасной передачи.
 * 
 * Используется для:
 * - Всех операций получения пользователей
 * - API ответов с пользовательскими данными  
 * - Отображения информации о пользователях в UI
 * - Логирования и аудита без утечки паролей
 * 
 * Особенности:
 * - НЕ содержит пароль для максимальной безопасности
 * - Включает все необходимые данные для отображения
 * - Поддерживает Lombok для генерации геттеров/сеттеров
 * - Оптимизирован для сериализации в JSON
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReadDto {
    
    /** Уникальный идентификатор пользователя */
    private Long id;
    
    /** Уникальное имя пользователя для входа в систему */
    private String username;
    
    /** Email адрес пользователя */
    private String email;
    
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
    
    /** Дата и время последнего обновления данных пользователя */
    private LocalDateTime updatedAt;
}
