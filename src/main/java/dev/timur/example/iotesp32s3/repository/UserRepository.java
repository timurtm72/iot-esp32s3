package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.User;
import dev.timur.example.iotesp32s3.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями в системе IoT.
 * Предоставляет современные методы для поиска, фильтрации и управления пользователями
 * с использованием Query Methods, Specifications и оптимизированных нативных запросов.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    // Query Methods - автогенерация запросов
    
    /**
     * Поиск пользователя по имени пользователя (username).
     * 
     * @param username имя пользователя для поиска
     * @return Optional содержащий пользователя, если найден
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Поиск пользователя по email адресу.
     * 
     * @param email email адрес для поиска
     * @return Optional содержащий пользователя, если найден
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Получение списка всех активных пользователей.
     * Заменяет старый @Query("SELECT u FROM User u WHERE u.active = true")
     * 
     * @return список активных пользователей
     */
    List<User> findByActiveTrue();
    
    /**
     * Получение списка всех активных пользователей, отсортированных по дате создания (новые первые).
     * 
     * @return список активных пользователей, отсортированный по дате создания
     */
    List<User> findByActiveTrueOrderByCreatedAtDesc();
    
    /**
     * Получение активных пользователей с поддержкой пагинации.
     * 
     * @param pageable параметры пагинации (номер страницы, размер страницы, сортировка)
     * @return страница активных пользователей
     */
    Page<User> findByActiveTrue(Pageable pageable);
    
    /**
     * Получение активных пользователей, созданных после указанной даты.
     * 
     * @param date дата, после которой были созданы пользователи
     * @return список активных пользователей, созданных после указанной даты
     */
    List<User> findByActiveTrueAndCreatedAtAfter(LocalDateTime date);
    
    /**
     * Поиск пользователей по роли.
     * 
     * @param role роль пользователя
     * @return список пользователей с указанной ролью
     */
    List<User> findByRole(Role role);
    
    /**
     * Поиск активных пользователей по роли.
     * 
     * @param role роль пользователя
     * @return список активных пользователей с указанной ролью
     */
    List<User> findByActiveTrueAndRole(Role role);
    
    /**
     * Подсчет количества активных пользователей с указанной ролью.
     * 
     * @param role роль пользователя
     * @return количество активных пользователей с указанной ролью
     */
    long countByActiveTrueAndRole(Role role);
    
    // Составные запросы
    
    /**
     * Поиск активного пользователя по имени пользователя.
     * 
     * @param username имя пользователя для поиска
     * @return Optional содержащий активного пользователя, если найден
     */
    Optional<User> findByUsernameAndActiveTrue(String username);
    
    /**
     * Поиск активного пользователя по email адресу.
     * 
     * @param email email адрес для поиска  
     * @return Optional содержащий активного пользователя, если найден
     */
    Optional<User> findByEmailAndActiveTrue(String email);
    
    /**
     * Оптимизированный поиск активных пользователей с ранжированием результатов.
     * Использует PostgreSQL ILIKE для нечувствительного к регистру поиска.
     * Результаты ранжируются по релевантности: точное совпадение в начале имени > email > другие поля.
     * 
     * @param query поисковый запрос для поиска в username, email, firstName, lastName
     * @return список найденных активных пользователей, отсортированный по релевантности
     */
    @Query(value = """
        SELECT * FROM users u 
        WHERE u.active = true 
        AND (u.username ILIKE %:query% 
             OR u.email ILIKE %:query% 
             OR u.first_name ILIKE %:query% 
             OR u.last_name ILIKE %:query%)
        ORDER BY 
            CASE WHEN u.username ILIKE :query% THEN 1
                 WHEN u.email ILIKE :query% THEN 2
                 ELSE 3 END,
            u.created_at DESC
        """, nativeQuery = true)
    List<User> searchActiveUsers(@Param("query") String query);
    
    /**
     * Пагинированный поиск активных пользователей.
     * 
     * @param query поисковый запрос для поиска в полях пользователя
     * @param pageable параметры пагинации
     * @return страница найденных активных пользователей
     */
    @Query(value = """
        SELECT * FROM users u 
        WHERE u.active = true 
        AND (u.username ILIKE %:query% 
             OR u.email ILIKE %:query% 
             OR u.first_name ILIKE %:query% 
             OR u.last_name ILIKE %:query%)
        ORDER BY u.created_at DESC
        """, nativeQuery = true)
    Page<User> searchActiveUsers(@Param("query") String query, Pageable pageable);
    
    /**
     * Получение базовой информации об активных пользователях (только id, username, email).
     * Используется для оптимизации - возвращает только необходимые поля.
     * 
     * @return список массивов объектов с базовой информацией о пользователях
     */
    @Query("SELECT u.id, u.username, u.email FROM User u WHERE u.active = true")
    List<Object[]> findActiveUsersBasicInfo();
    
    // Подсчеты
    
    /**
     * Подсчет количества активных пользователей в системе.
     * 
     * @return количество активных пользователей
     */
    long countByActiveTrue();
    
    /**
     * Подсчет количества активных пользователей, созданных после указанной даты.
     * 
     * @param date дата, после которой считаются пользователи
     * @return количество активных пользователей, созданных после даты
     */
    long countByActiveTrueAndCreatedAtAfter(LocalDateTime date);
    
    // Exists методы (более оптимизированные чем count > 0)
    
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
    
    /**
     * Проверка существования активного пользователя с указанным именем пользователя.
     * 
     * @param username имя пользователя для проверки
     * @return true, если активный пользователь с таким username существует
     */
    boolean existsByUsernameAndActiveTrue(String username);
    
    /**
     * Проверка существования активного пользователя с указанным email адресом.
     * 
     * @param email email адрес для проверки
     * @return true, если активный пользователь с таким email существует
     */
    boolean existsByEmailAndActiveTrue(String email);
} 