package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.Device;
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
 * Репозиторий для управления IoT устройствами в системе.
 * Предоставляет современные методы для поиска, фильтрации и управления устройствами
 * с поддержкой мягкого удаления (soft delete) через поле removedAt.
 * Использует Query Methods, Specifications и оптимизированные нативные запросы.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {
    
    // Query Methods - заменяем @Query на автогенерацию
    
    /**
     * Поиск активного устройства по ID (не удаленного).
     * 
     * @param id идентификатор устройства
     * @return Optional содержащий устройство, если найдено и не удалено
     */
    Optional<Device> findByIdAndRemovedAtIsNull(Long id);
    
    /**
     * Получение списка всех активных устройств (не удаленных).
     * 
     * @return список активных устройств
     */
    List<Device> findByRemovedAtIsNull();
    
    /**
     * Получение активных устройств с поддержкой пагинации.
     * 
     * @param pageable параметры пагинации (номер страницы, размер страницы, сортировка)
     * @return страница активных устройств
     */
    Page<Device> findByRemovedAtIsNull(Pageable pageable);
    
    /**
     * Получение активных устройств, отсортированных по дате создания (новые первые).
     * 
     * @return список активных устройств, отсортированный по дате создания
     */
    List<Device> findByRemovedAtIsNullOrderByCreatedAtDesc();
    
    /**
     * Поиск активных устройств по названию (без учета регистра).
     * 
     * @param name часть названия устройства для поиска
     * @return список найденных активных устройств
     */
    List<Device> findByNameContainingIgnoreCaseAndRemovedAtIsNull(String name);
    
    /**
     * Пагинированный поиск активных устройств по названию (без учета регистра).
     * 
     * @param name часть названия устройства для поиска
     * @param pageable параметры пагинации
     * @return страница найденных активных устройств
     */
    Page<Device> findByNameContainingIgnoreCaseAndRemovedAtIsNull(String name, Pageable pageable);
    
    /**
     * Поиск активных устройств по местоположению (без учета регистра).
     * 
     * @param location часть местоположения для поиска
     * @return список найденных активных устройств
     */
    List<Device> findByLocationContainingIgnoreCaseAndRemovedAtIsNull(String location);
    
    /**
     * Пагинированный поиск активных устройств по местоположению (без учета регистра).
     * 
     * @param location часть местоположения для поиска
     * @param pageable параметры пагинации
     * @return страница найденных активных устройств
     */
    Page<Device> findByLocationContainingIgnoreCaseAndRemovedAtIsNull(String location, Pageable pageable);
    
    // Составные запросы
    
    /**
     * Поиск активных устройств по названию и местоположению одновременно.
     * 
     * @param name часть названия устройства
     * @param location часть местоположения
     * @return список найденных активных устройств
     */
    List<Device> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCaseAndRemovedAtIsNull(
        String name, String location);
    
    /**
     * Получение активных устройств, созданных после указанной даты.
     * 
     * @param date дата, после которой были созданы устройства
     * @return список активных устройств, созданных после даты
     */
    List<Device> findByCreatedAtAfterAndRemovedAtIsNull(LocalDateTime date);
    
    /**
     * Получение активных устройств, созданных в указанном диапазоне дат.
     * 
     * @param start начальная дата диапазона
     * @param end конечная дата диапазона
     * @return список активных устройств, созданных в диапазоне
     */
    List<Device> findByCreatedAtBetweenAndRemovedAtIsNull(LocalDateTime start, LocalDateTime end);
    
    /**
     * Оптимизированный поиск активных устройств с ранжированием результатов.
     * Использует PostgreSQL ILIKE для нечувствительного к регистру поиска.
     * Поиск ведется по полям: name, description, location.
     * Результаты ранжируются по релевантности: точное совпадение в name > location > description.
     * 
     * @param query поисковый запрос для поиска в полях устройства
     * @return список найденных активных устройств, отсортированный по релевантности
     */
    @Query(value = """
        SELECT d.* FROM device d 
        WHERE d.removed_at IS NULL 
        AND (d.name ILIKE %:query% 
             OR d.description ILIKE %:query% 
             OR d.location ILIKE %:query%)
        ORDER BY 
            CASE WHEN d.name ILIKE :query% THEN 1
                 WHEN d.location ILIKE :query% THEN 2
                 ELSE 3 END,
            d.created_at DESC
        """, nativeQuery = true)
    List<Device> searchActiveDevices(@Param("query") String query);
    
    /**
     * Пагинированный поиск активных устройств.
     * 
     * @param query поисковый запрос для поиска в полях устройства
     * @param pageable параметры пагинации
     * @return страница найденных активных устройств
     */
    @Query(value = """
        SELECT d.* FROM device d 
        WHERE d.removed_at IS NULL 
        AND (d.name ILIKE %:query% 
             OR d.description ILIKE %:query% 
             OR d.location ILIKE %:query%)
        ORDER BY d.created_at DESC
        """, nativeQuery = true)
    Page<Device> searchActiveDevices(@Param("query") String query, Pageable pageable);
    
    // Статистические запросы
    
    /**
     * Подсчет количества активных устройств в системе.
     * 
     * @return количество активных устройств
     */
    long countByRemovedAtIsNull();
    
    /**
     * Подсчет количества активных устройств, созданных после указанной даты.
     * 
     * @param date дата, после которой считаются устройства
     * @return количество активных устройств, созданных после даты
     */
    long countByCreatedAtAfterAndRemovedAtIsNull(LocalDateTime date);
    
    /**
     * Подсчет количества активных устройств с названием, содержащим указанный текст.
     * 
     * @param name текст для поиска в названии
     * @return количество найденных активных устройств
     */
    long countByNameContainingIgnoreCaseAndRemovedAtIsNull(String name);
    
    /**
     * Получение базовой информации об активных устройствах (только id, name, location).
     * Используется для оптимизации - возвращает только необходимые поля.
     * 
     * @return список массивов объектов с базовой информацией об устройствах
     */
    @Query("SELECT d.id, d.name, d.location FROM Device d WHERE d.removedAt IS NULL")
    List<Object[]> findActiveDevicesBasicInfo();
    
    // Exists методы
    
    /**
     * Проверка существования активного устройства с указанным названием.
     * 
     * @param name название устройства для проверки
     * @return true, если активное устройство с таким названием существует
     */
    boolean existsByNameAndRemovedAtIsNull(String name);
    
    /**
     * Проверка существования активного устройства с указанным названием, исключая устройство с указанным ID.
     * Полезно при обновлении устройства для проверки уникальности названия.
     * 
     * @param name название устройства для проверки
     * @param id ID устройства, которое нужно исключить из проверки
     * @return true, если другое активное устройство с таким названием существует
     */
    boolean existsByNameAndIdNotAndRemovedAtIsNull(String name, Long id);
    
    // Методы для работы с данными устройств
    
    /**
     * Получение устройства с загруженными данными (EAGER fetch).
     * Использует LEFT JOIN FETCH для оптимизации загрузки связанных данных.
     * 
     * @param id идентификатор устройства
     * @return Optional содержащий устройство с данными, если найдено
     */
    @Query("SELECT d FROM Device d LEFT JOIN FETCH d.dataValues WHERE d.id = :id AND d.removedAt IS NULL")
    Optional<Device> findByIdWithDataAndRemovedAtIsNull(@Param("id") Long id);
    
    /**
     * Получение активных устройств с данными за указанный период.
     * Загружает только те устройства, у которых есть данные после указанной даты.
     * 
     * @param since дата, после которой должны быть данные устройства
     * @return список активных устройств с недавними данными
     */
    @Query("SELECT d FROM Device d LEFT JOIN FETCH d.dataValues dv WHERE d.removedAt IS NULL AND dv.timestamp >= :since")
    List<Device> findActiveDevicesWithRecentData(@Param("since") LocalDateTime since);
} 