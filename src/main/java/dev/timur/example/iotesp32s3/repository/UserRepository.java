package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.enums.Role;
import dev.timur.example.iotesp32s3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями пользователей
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Поиск всех активных пользователей (не удалённых)
     * @return список активных пользователей
     */
    List<User> findByRemovedAtIsNull();
    
    /**
     * Поиск пользователя по имени пользователя среди активных
     * @param username имя пользователя
     * @return опциональный пользователь
     */
    Optional<User> findByUsernameAndRemovedAtIsNull(String username);
    
    /**
     * Поиск пользователя по электронной почте среди активных
     * @param email электронная почта
     * @return опциональный пользователь
     */
    Optional<User> findByEmailAndRemovedAtIsNull(String email);
    
    /**
     * Поиск активных пользователей по роли
     * @param role роль пользователя
     * @return список пользователей с указанной ролью
     */
    List<User> findByRoleAndRemovedAtIsNull(Role role);
    
    /**
     * Поиск активных пользователей по статусу активности
     * @param active статус активности
     * @return список пользователей с указанным статусом
     */
    List<User> findByActiveAndRemovedAtIsNull(Boolean active);
    
    /**
     * Поиск пользователей с последним входом после указанной даты
     * @param lastLoginAfter дата последнего входа
     * @return список пользователей
     */
    List<User> findByLastLoginAfterAndRemovedAtIsNull(LocalDateTime lastLoginAfter);
} 