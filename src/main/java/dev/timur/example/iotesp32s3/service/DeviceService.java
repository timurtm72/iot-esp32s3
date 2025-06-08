package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервисный интерфейс для управления IoT устройствами в системе.
 * Предоставляет методы для создания, обновления, удаления и поиска устройств
 * с поддержкой мягкого удаления, фильтрации по различным критериям и пагинации.
 * Интегрируется с системой телеметрии для получения актуального состояния устройств.
 */
public interface DeviceService {
    
    // Основные CRUD операции
    
    /**
     * Получение устройства по идентификатору.
     * Возвращает только активные (не удаленные) устройства.
     * 
     * @param id уникальный идентификатор устройства
     * @return DeviceDto с данными устройства
     * @throws RuntimeException если устройство не найдено или удалено
     */
    DeviceDto getById(Long id);
    
    /**
     * Получение списка всех активных устройств в системе.
     * 
     * @return список всех активных устройств
     */
    List<DeviceDto> getAll();
    
    /**
     * Создание нового IoT устройства в системе.
     * 
     * @param deviceDto данные для создания устройства
     * @return статус операции (SUCCESS/ERROR)
     */
    Status create(DeviceDto deviceDto);
    
    /**
     * Обновление данных существующего устройства.
     * 
     * @param deviceDto новые данные устройства
     * @param id идентификатор обновляемого устройства
     * @return статус операции (SUCCESS/ERROR)
     */
    Status update(DeviceDto deviceDto, Long id);
    
    /**
     * Удаление устройства по идентификатору.
     * Выполняет мягкое удаление (установка removedAt).
     * 
     * @param id идентификатор удаляемого устройства
     * @return статус операции (SUCCESS/ERROR)
     */
    Status delete(Long id);
    
    // Методы поиска
    
    /**
     * Поиск активных устройств по части названия.
     * Поиск выполняется без учета регистра.
     * 
     * @param name часть названия устройства для поиска
     * @return список найденных устройств
     */
    List<DeviceDto> findByNameContaining(String name);
    
    /**
     * Поиск активных устройств по части местоположения.
     * Поиск выполняется без учета регистра.
     * 
     * @param location часть местоположения для поиска
     * @return список найденных устройств
     */
    List<DeviceDto> findByLocationContaining(String location);
    
    // Расширенные методы с пагинацией
    
    /**
     * Получение активных устройств с поддержкой пагинации.
     * 
     * @param pageable параметры пагинации (номер страницы, размер, сортировка)
     * @return страница активных устройств
     */
    Page<DeviceDto> getAllActive(Pageable pageable);
    
    /**
     * Пагинированный поиск устройств по названию.
     * 
     * @param name часть названия для поиска
     * @param pageable параметры пагинации
     * @return страница найденных устройств
     */
    Page<DeviceDto> findByNameContaining(String name, Pageable pageable);
    
    /**
     * Пагинированный поиск устройств по местоположению.
     * 
     * @param location часть местоположения для поиска
     * @param pageable параметры пагинации
     * @return страница найденных устройств
     */
    Page<DeviceDto> findByLocationContaining(String location, Pageable pageable);
    
    /**
     * Универсальный текстовый поиск по основным полям устройства.
     * Поиск ведется по полям: name, description, location.
     * 
     * @param searchText поисковый запрос
     * @return список найденных устройств
     */
    List<DeviceDto> searchDevices(String searchText);
    
    /**
     * Пагинированный универсальный поиск устройств.
     * 
     * @param searchText поисковый запрос
     * @param pageable параметры пагинации
     * @return страница найденных устройств
     */
    Page<DeviceDto> searchDevices(String searchText, Pageable pageable);
    
    // Методы фильтрации по времени
    
    /**
     * Поиск устройств, созданных после указанной даты.
     * 
     * @param date дата, после которой созданы устройства
     * @return список устройств, созданных после указанной даты
     */
    List<DeviceDto> findDevicesCreatedAfter(LocalDateTime date);
    
    /**
     * Поиск устройств, созданных в указанном диапазоне дат.
     * 
     * @param start начальная дата диапазона
     * @param end конечная дата диапазона
     * @return список устройств, созданных в диапазоне
     */
    List<DeviceDto> findDevicesCreatedBetween(LocalDateTime start, LocalDateTime end);
    
    // Комплексный поиск с фильтрами
    
    /**
     * Комплексный поиск устройств с множественными фильтрами и пагинацией.
     * Объединяет различные критерии поиска для гибкой фильтрации.
     * 
     * @param searchText общий текст для поиска (может быть null)
     * @param name фильтр по названию (может быть null)
     * @param location фильтр по местоположению (может быть null)
     * @param description фильтр по описанию (может быть null)
     * @param createdAfter дата, после которой созданы устройства (может быть null)
     * @param createdBefore дата, до которой созданы устройства (может быть null)
     * @param pageable параметры пагинации
     * @return страница устройств, соответствующих критериям
     */
    Page<DeviceDto> findDevicesWithFilters(
        String searchText,
        String name,
        String location,
        String description,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore,
        Pageable pageable
    );
    
    // Интеграция с телеметрией
    
    /**
     * Получение устройства с последними данными телеметрии.
     * 
     * @param id идентификатор устройства
     * @return DeviceDto с включенными последними данными
     */
    DeviceDto getDeviceWithLatestData(Long id);
    
    /**
     * Поиск устройств, которые передавали данные после указанной даты.
     * Полезно для определения активных устройств.
     * 
     * @param since дата, после которой устройства должны были передавать данные
     * @return список активно работающих устройств
     */
    List<DeviceDto> findDevicesWithRecentData(LocalDateTime since);
    
    // Проверки существования
    
    /**
     * Проверка существования активного устройства с указанным названием.
     * 
     * @param name название устройства для проверки
     * @return true, если активное устройство с таким названием существует
     */
    boolean existsByName(String name);
    
    /**
     * Проверка существования активного устройства с указанным названием, исключая устройство с указанным ID.
     * Полезно при обновлении устройства для проверки уникальности названия.
     * 
     * @param name название устройства для проверки
     * @param excludeId ID устройства, которое нужно исключить из проверки
     * @return true, если другое активное устройство с таким названием существует
     */
    boolean existsByNameAndIdNot(String name, Long excludeId);
    
    // Статистические методы
    
    /**
     * Подсчет количества активных устройств в системе.
     * 
     * @return количество активных устройств
     */
    long countActiveDevices();
    
    /**
     * Подсчет количества активных устройств, созданных после указанной даты.
     * 
     * @param date дата, после которой считаются устройства
     * @return количество активных устройств, созданных после даты
     */
    long countDevicesCreatedAfter(LocalDateTime date);
    
    /**
     * Подсчет количества активных устройств с названием, содержащим указанный текст.
     * 
     * @param name текст для поиска в названии
     * @return количество найденных активных устройств
     */
    long countDevicesByNameContaining(String name);
} 