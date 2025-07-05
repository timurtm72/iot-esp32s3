package dev.timur.example.iotesp32s3.dto;

import dev.timur.example.iotesp32s3.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для представления данных пользователя с паролем
 * Используется для создания и обновления пользователей
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    /** Уникальный идентификатор пользователя */
    private Long id;
    
    /** Имя пользователя */
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
    
    /** Электронная почта пользователя */
    @Email
    @NotBlank
    private String email;

    /** Пароль пользователя */
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;
    
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