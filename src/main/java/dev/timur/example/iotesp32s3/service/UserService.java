package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.UserDto;
import dev.timur.example.iotesp32s3.dto.UserReadDto;
import dev.timur.example.iotesp32s3.enums.Role;
import dev.timur.example.iotesp32s3.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервисный интерфейс для управления пользователями в системе IoT.
 * Предоставляет методы для создания, обновления, удаления и поиска пользователей
 * с поддержкой ролей, фильтрации, пагинации и статистики.
 * Использует UserReadDto для операций чтения (скрывает пароли) и UserDto для операций записи.
 */
public interface UserService {
    
    // Основные CRUD операции
    
    /**
     * Получение пользователя по идентификатору.
     * 
     * @param id уникальный идентификатор пользователя
     * @return UserReadDto с данными пользователя (без пароля)
     * @throws RuntimeException если пользователь не найден
     */
    UserReadDto getById(Long id);
    
    /**
     * Получение списка всех пользователей в системе.
     * 
     * @return список всех пользователей в формате UserReadDto
     */
    List<UserReadDto> getAll();
    
    /**
     * Создание нового пользователя в системе.
     * 
     * @param userDto данные для создания пользователя
     * @param password пароль пользователя (будет зашифрован)
     * @return статус операции (SUCCESS/ERROR)
     */
    Status create(UserDto userDto, String password);
    
    /**
     * Обновление данных существующего пользователя.
     * 
     * @param userDto новые данные пользователя
     * @param id идентификатор обновляемого пользователя
     * @return статус операции (SUCCESS/ERROR)
     */
    Status update(UserDto userDto, Long id);
    
    /**
     * Удаление пользователя по идентификатору.
     * Выполняет мягкое удаление (деактивация).
     * 
     * @param id идентификатор удаляемого пользователя
     * @return статус операции (SUCCESS/ERROR)
     */
    Status delete(Long id);
    
    // Методы поиска
    
    /**
     * Поиск пользователя по имени пользователя.
     * 
     * @param username имя пользователя для поиска
     * @return UserReadDto найденного пользователя или null
     */
    UserReadDto findByUsername(String username);
    
    /**
     * Поиск пользователя по email адресу.
     * 
     * @param email email адрес для поиска
     * @return UserReadDto найденного пользователя или null
     */
    UserReadDto findByEmail(String email);
    
    /**
     * Получение списка всех активных пользователей.
     * 
     * @return список активных пользователей
     */
    List<UserReadDto> findAllActiveUsers();
    
    /**
     * Текстовый поиск пользователей по основным полям.
     * Поиск ведется по username, email, firstName, lastName.
     * 
     * @param query поисковый запрос
     * @return список найденных пользователей
     */
    List<UserReadDto> searchUsers(String query);
    
    // Методы проверки существования
    
    /**
     * Проверка существования пользователя с указанным именем пользователя.
     * 
     * @param username имя пользователя для проверки
     * @return true, если пользователь с таким username существует
     */
    boolean existsByUsername(String username);
    
    /**
     * Проверка существования пользователя с указанным email адресом.
     * 
     * @param email email адрес для проверки
     * @return true, если пользователь с таким email существует
     */
    boolean existsByEmail(String email);
    
    // Новые современные методы с пагинацией
    
    /**
     * Получение активных пользователей с поддержкой пагинации.
     * 
     * @param pageable параметры пагинации (номер страницы, размер, сортировка)
     * @return страница активных пользователей
     */
    Page<UserReadDto> findAllActiveUsers(Pageable pageable);
    
    /**
     * Пагинированный текстовый поиск пользователей.
     * 
     * @param query поисковый запрос
     * @param pageable параметры пагинации
     * @return страница найденных пользователей
     */
    Page<UserReadDto> searchUsers(String query, Pageable pageable);
    
    // Методы фильтрации по ролям
    
    /**
     * Поиск пользователей по роли.
     * 
     * @param role роль пользователей для поиска
     * @return список пользователей с указанной ролью
     */
    List<UserReadDto> findUsersByRole(Role role);
    
    /**
     * Поиск активных пользователей по роли.
     * 
     * @param role роль активных пользователей для поиска
     * @return список активных пользователей с указанной ролью
     */
    List<UserReadDto> findActiveUsersByRole(Role role);
    
    // Методы фильтрации по времени
    
    /**
     * Поиск пользователей, созданных после указанной даты.
     * 
     * @param date дата, после которой созданы пользователи
     * @return список пользователей, созданных после указанной даты
     */
    List<UserReadDto> findUsersCreatedAfter(LocalDateTime date);
    
    // Комплексный поиск с фильтрами
    
    /**
     * Комплексный поиск пользователей с множественными фильтрами и пагинацией.
     * Объединяет различные критерии поиска для гибкой фильтрации.
     * 
     * @param active статус активности (true/false/null - игнорировать)
     * @param role роль пользователя (может быть null)
     * @param searchText текст для поиска в полях пользователя (может быть null)
     * @param createdAfter дата, после которой созданы пользователи (может быть null)
     * @param createdBefore дата, до которой созданы пользователи (может быть null)
     * @param pageable параметры пагинации
     * @return страница пользователей, соответствующих критериям
     */
    Page<UserReadDto> findUsersWithFilters(
        Boolean active, 
        Role role, 
        String searchText,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore,
        Pageable pageable
    );
    
    // Статистические методы
    
    /**
     * Подсчет количества активных пользователей в системе.
     * 
     * @return количество активных пользователей
     */
    long countActiveUsers();
    
    /**
     * Подсчет количества пользователей с указанной ролью.
     * 
     * @param role роль для подсчета пользователей
     * @return количество пользователей с указанной ролью
     */
    long countUsersByRole(Role role);
} 