package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.UserDto;
import dev.timur.example.iotesp32s3.dto.UserReadDto;
import dev.timur.example.iotesp32s3.model.User;
import org.mapstruct.Mapper;

/**
 * MapStruct маппер для преобразования между моделью User и соответствующими DTO.
 * Автоматически генерирует безопасные методы преобразования без утечки конфиденциальных данных.
 * 
 * Особенности:
 * - Конфигурирован как Spring компонент для автоматического внедрения
 * - UserReadDto исключает пароль из операций чтения для безопасности
 * - Автоматическое маппинг полей по именам
 * - Compile-time проверка совместимости типов
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    /**
     * Преобразует модель пользователя в DTO для передачи данных.
     * Включает все поля, включая пароль (используется осторожно).
     * 
     * @param user модель пользователя из базы данных
     * @return UserDto с данными пользователя
     */
    UserDto toDto(User user);
    
    /**
     * Преобразует DTO пользователя в модель для сохранения в базе данных.
     * 
     * @param userDto DTO с данными пользователя
     * @return модель User для работы с базой данных
     */
    User toEntity(UserDto userDto);
    
    /**
     * Преобразует модель пользователя в безопасный DTO для чтения.
     * Автоматически исключает пароль и другие конфиденциальные данные.
     * Используется для всех операций получения пользователей.
     * 
     * @param user модель пользователя из базы данных
     * @return UserReadDto без конфиденциальных данных (пароля)
     */
    UserReadDto toReadDto(User user);
} 