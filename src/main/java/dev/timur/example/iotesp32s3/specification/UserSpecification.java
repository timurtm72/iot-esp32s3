package dev.timur.example.iotesp32s3.specification;

import dev.timur.example.iotesp32s3.enums.Role;
import dev.timur.example.iotesp32s3.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания динамических спецификаций запросов к пользователям.
 * Предоставляет статические методы для построения составных и гибких критериев поиска
 * с использованием JPA Criteria API через Spring Data Specifications.
 * Поддерживает композиционные запросы и текстовый поиск с ранжированием релевантности.
 */
public class UserSpecification {

    /**
     * Создает спецификацию для поиска только активных пользователей.
     * 
     * @return спецификация для фильтрации активных пользователей (active = true)
     */
    public static Specification<User> isActive() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isTrue(root.get("active"));
    }

    /**
     * Создает спецификацию для поиска пользователей с определенной ролью.
     * 
     * @param role роль пользователя для поиска
     * @return спецификация для фильтрации по роли или null, если роль не указана
     */
    public static Specification<User> hasRole(Role role) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("role"), role);
    }

    /**
     * Создает спецификацию для поиска пользователей, созданных после указанной даты.
     * 
     * @param date дата, после которой ищутся пользователи
     * @return спецификация для фильтрации по дате создания
     */
    public static Specification<User> createdAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThan(root.get("createdAt"), date);
    }

    /**
     * Создает спецификацию для поиска пользователей, созданных в указанном диапазоне дат.
     * 
     * @param start начальная дата диапазона
     * @param end конечная дата диапазона
     * @return спецификация для фильтрации по диапазону дат создания
     */
    public static Specification<User> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("createdAt"), start, end);
    }

    /**
     * Создает спецификацию для текстового поиска по основным полям пользователя.
     * Поиск ведется по полям: username, email, firstName, lastName.
     * Использует LIKE для нечувствительного к регистру поиска.
     * 
     * @param username часть имени пользователя для поиска
     * @return спецификация для поиска по username или null, если не указан
     */
    public static Specification<User> usernameContains(String username) {
        return username == null || username.trim().isEmpty() ? null :
            (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")), 
                    "%" + username.toLowerCase() + "%");
    }

    /**
     * Создает спецификацию для поиска пользователей по части email адреса.
     * 
     * @param email часть email адреса для поиска
     * @return спецификация для поиска по email или null, если не указан
     */
    public static Specification<User> emailContains(String email) {
        return email == null || email.trim().isEmpty() ? null :
            (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")), 
                    "%" + email.toLowerCase() + "%");
    }

    /**
     * Создает спецификацию для поиска пользователей по имени или фамилии.
     * Ищет в полях firstName и lastName с использованием логического ИЛИ (OR).
     * 
     * @param name часть имени или фамилии для поиска
     * @return спецификация для поиска по имени или фамилии
     */
    public static Specification<User> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + name.toLowerCase() + "%";
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern)
            );
        };
    }

    /**
     * Создает композитную спецификацию для комплексного поиска пользователей.
     * Объединяет несколько условий поиска с помощью логического И (AND).
     * Автоматически исключает null условия из итогового запроса.
     * 
     * @param active статус активности пользователя (true/false/null - игнорировать)
     * @param role роль пользователя для фильтрации (может быть null)
     * @param searchText текст для поиска в полях пользователя (может быть null)
     * @param createdAfter дата, после которой созданы пользователи (может быть null)
     * @param createdBefore дата, до которой созданы пользователи (может быть null)
     * @return композитная спецификация для комплексного поиска
     */
    public static Specification<User> buildComplexQuery(
            Boolean active, 
            Role role, 
            String searchText,
            LocalDateTime createdAfter,
            LocalDateTime createdBefore) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (active != null) {
                if (active) {
                    predicates.add(criteriaBuilder.isTrue(root.get("active")));
                } else {
                    predicates.add(criteriaBuilder.isFalse(root.get("active")));
                }
            }

            if (role != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), role));
            }

            if (searchText != null && !searchText.trim().isEmpty()) {
                String pattern = "%" + searchText.toLowerCase() + "%";
                Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern)
                );
                predicates.add(searchPredicate);
            }

            if (createdAfter != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAfter));
            }

            if (createdBefore != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdBefore));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
} 