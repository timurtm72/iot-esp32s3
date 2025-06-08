package dev.timur.example.iotesp32s3.specification;

import dev.timur.example.iotesp32s3.model.Device;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания динамических спецификаций запросов к IoT устройствам.
 * Предоставляет статические методы для построения составных и гибких критериев поиска
 * с использованием JPA Criteria API через Spring Data Specifications.
 * Поддерживает фильтрацию по активности устройств (мягкое удаление через removedAt),
 * поиск по названию, местоположению и комплексные композитные запросы.
 */
public class DeviceSpecification {

    /**
     * Создает спецификацию для поиска только активных устройств (не удаленных).
     * Фильтрует устройства, у которых поле removedAt равно null.
     * 
     * @return спецификация для фильтрации активных устройств
     */
    public static Specification<Device> isActive() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isNull(root.get("removedAt"));
    }

    public static Specification<Device> isRemoved() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isNotNull(root.get("removedAt"));
    }

    /**
     * Создает спецификацию для поиска устройств по части названия.
     * Выполняет поиск без учета регистра с использованием LIKE.
     * 
     * @param name часть названия устройства для поиска
     * @return спецификация для поиска по названию или null, если название не указано
     */
    public static Specification<Device> nameContains(String name) {
        return name == null || name.trim().isEmpty() ? null :
            (root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), 
                    "%" + name.toLowerCase() + "%"
                );
    }

    /**
     * Создает спецификацию для поиска устройств по части местоположения.
     * Выполняет поиск без учета регистра с использованием LIKE.
     * 
     * @param location часть местоположения устройства для поиска
     * @return спецификация для поиска по местоположению или null, если местоположение не указано
     */
    public static Specification<Device> locationContains(String location) {
        return location == null || location.trim().isEmpty() ? null :
            (root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")), 
                    "%" + location.toLowerCase() + "%"
                );
    }

    /**
     * Создает спецификацию для поиска устройств по части описания.
     * Выполняет поиск без учета регистра с использованием LIKE.
     * 
     * @param description часть описания устройства для поиска
     * @return спецификация для поиска по описанию или null, если описание не указано
     */
    public static Specification<Device> descriptionContains(String description) {
        return description == null || description.trim().isEmpty() ? null :
            (root, query, criteriaBuilder) -> 
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), 
                    "%" + description.toLowerCase() + "%"
                );
    }

    /**
     * Создает спецификацию для поиска устройств, созданных после указанной даты.
     * 
     * @param date дата, после которой ищутся устройства
     * @return спецификация для фильтрации по дате создания
     */
    public static Specification<Device> createdAfter(LocalDateTime date) {
        return date == null ? null :
            (root, query, criteriaBuilder) -> 
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), date);
    }

    /**
     * Создает спецификацию для поиска устройств, созданных до указанной даты.
     * 
     * @param date дата, до которой ищутся устройства
     * @return спецификация для фильтрации по дате создания
     */
    public static Specification<Device> createdBefore(LocalDateTime date) {
        return date == null ? null :
            (root, query, criteriaBuilder) -> 
                criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), date);
    }

    /**
     * Создает спецификацию для поиска устройств, созданных в указанном диапазоне дат.
     * 
     * @param start начальная дата диапазона
     * @param end конечная дата диапазона
     * @return спецификация для фильтрации по диапазону дат создания
     */
    public static Specification<Device> createdBetween(LocalDateTime start, LocalDateTime end) {
        return start == null || end == null ? null :
            (root, query, criteriaBuilder) -> 
                criteriaBuilder.between(root.get("createdAt"), start, end);
    }

    public static Specification<Device> modifiedAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThan(root.get("modifiedAt"), date);
    }

    /**
     * Создает текстовый поиск по основным полям устройства.
     * Поиск ведется по полям: name, description, location.
     * Использует логическое ИЛИ (OR) для объединения условий.
     * 
     * @param searchText текст для поиска в полях устройства
     * @return спецификация для текстового поиска или null, если текст не указан
     */
    public static Specification<Device> searchByText(String searchText) {
        return searchText == null || searchText.trim().isEmpty() ? null :
            (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchText.toLowerCase() + "%";
                return criteriaBuilder.or(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), likePattern),
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")), likePattern)
                );
            };
    }

    // Устройства с данными за определенный период
    public static Specification<Device> hasDataAfter(LocalDateTime timestamp) {
        return (root, query, criteriaBuilder) -> {
            var subquery = query.subquery(Long.class);
            var dataRoot = subquery.from(root.getJavaType());
            subquery.select(criteriaBuilder.count(dataRoot.get("id")))
                   .where(
                       criteriaBuilder.equal(dataRoot.get("device"), root),
                       criteriaBuilder.greaterThan(dataRoot.get("timestamp"), timestamp)
                   );
            return criteriaBuilder.greaterThan(subquery, 0L);
        };
    }

    /**
     * Создает композитную спецификацию для комплексного поиска устройств.
     * Объединяет несколько условий поиска с помощью логического И (AND).
     * Автоматически исключает null условия из итогового запроса.
     * По умолчанию ищет только активные устройства.
     * 
     * @param searchText текст для поиска в полях устройства (может быть null)
     * @param name фильтр по названию устройства (может быть null)
     * @param location фильтр по местоположению (может быть null)
     * @param description фильтр по описанию (может быть null)
     * @param createdAfter дата, после которой созданы устройства (может быть null)
     * @param createdBefore дата, до которой созданы устройства (может быть null)
     * @param activeOnly если true - искать только активные устройства (по умолчанию true)
     * @return композитная спецификация для комплексного поиска
     */
    public static Specification<Device> buildComplexQuery(
            String searchText,
            String name,
            String location,
            String description,
            LocalDateTime createdAfter,
            LocalDateTime createdBefore,
            boolean activeOnly) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // По умолчанию ищем только активные устройства
            if (activeOnly) {
                predicates.add(criteriaBuilder.isNull(root.get("removedAt")));
            }
            
            // Добавляем общий текстовый поиск если указан
            if (searchText != null && !searchText.trim().isEmpty()) {
                String pattern = "%" + searchText.toLowerCase() + "%";
                Predicate textSearchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), pattern)
                );
                predicates.add(textSearchPredicate);
            }
            
            // Специфичные фильтры по полям
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), 
                    "%" + name.toLowerCase() + "%"));
            }
            
            if (location != null && !location.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")), 
                    "%" + location.toLowerCase() + "%"));
            }
            
            if (description != null && !description.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), 
                    "%" + description.toLowerCase() + "%"));
            }
            
            // Фильтры по датам
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