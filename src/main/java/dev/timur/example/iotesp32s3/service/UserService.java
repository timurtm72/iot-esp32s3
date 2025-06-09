package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.UserDto;
import dev.timur.example.iotesp32s3.dto.UserReadDto;
import dev.timur.example.iotesp32s3.enums.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с пользователями
 */
public interface UserService {
    
    /**
     * Создание нового пользователя
     * @param userDto данные пользователя с паролем
     * @return созданный пользователь (без пароля)
     */
    UserReadDto createUser(UserDto userDto);
    
    /**
     * Получение пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return пользователь или пустой Optional (без пароля)
     */
    Optional<UserReadDto> getUserById(Long id);
    
    /**
     * Обновление данных пользователя
     * @param id идентификатор пользователя
     * @param userDto новые данные пользователя
     * @return обновленный пользователь (без пароля)
     */
    Optional<UserReadDto> updateUser(Long id, UserDto userDto);
    
    /**
     * Мягкое удаление пользователя
     * @param id идентификатор пользователя
     * @return true если пользователь удален, false если не найден
     */
    boolean deleteUser(Long id);
    
    /**
     * Получение всех активных пользователей
     * @return список активных пользователей (без паролей)
     */
    List<UserReadDto> getAllActiveUsers();
    
    /**
     * Поиск пользователя по имени пользователя
     * @param username имя пользователя
     * @return пользователь или пустой Optional (без пароля)
     */
    Optional<UserReadDto> getUserByUsername(String username);
    
    /**
     * Поиск пользователя по электронной почте
     * @param email электронная почта
     * @return пользователь или пустой Optional (без пароля)
     */
    Optional<UserReadDto> getUserByEmail(String email);
    
    /**
     * Получение пользователей по роли
     * @param role роль пользователя
     * @return список пользователей с указанной ролью (без паролей)
     */
    List<UserReadDto> getUsersByRole(Role role);
    
    /**
     * Получение пользователей по статусу активности
     * @param active статус активности
     * @return список пользователей с указанным статусом (без паролей)
     */
    List<UserReadDto> getUsersByActiveStatus(Boolean active);
    
    /**
     * Получение пользователей с последним входом после указанной даты
     * @param lastLoginAfter дата последнего входа
     * @return список пользователей (без паролей)
     */
    List<UserReadDto> getUsersWithLastLoginAfter(LocalDateTime lastLoginAfter);
} 