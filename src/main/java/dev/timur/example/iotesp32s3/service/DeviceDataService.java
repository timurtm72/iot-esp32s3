package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.DeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Сервисный интерфейс для управления данными телеметрии IoT устройств.
 * Предоставляет методы для сохранения, получения и анализа временных рядов данных
 * с поддержкой фильтрации, агрегации, статистических вычислений и обнаружения аномалий.
 * Оптимизирован для работы с большими объемами временных данных (time-series data).
 */
public interface DeviceDataService {
    
    // Основные CRUD операции
    
    /**
     * Получение записи данных устройства по идентификатору.
     * 
     * @param id уникальный идентификатор записи данных
     * @return DeviceDataDto с данными телеметрии
     * @throws RuntimeException если запись не найдена
     */
    DeviceDataDto getById(Long id);
    
    /**
     * Получение всех записей данных в системе.
     * Внимание: может вернуть большое количество записей, рекомендуется использовать пагинацию.
     * 
     * @return список всех записей данных
     */
    List<DeviceDataDto> getAll();
    
    /**
     * Создание новой записи данных телеметрии для устройства.
     * 
     * @param deviceDataDto данные телеметрии для сохранения
     * @param deviceId идентификатор устройства, к которому относятся данные
     * @return статус операции (SUCCESS/ERROR)
     */
    Status create(DeviceDataDto deviceDataDto, Long deviceId);
    
    /**
     * Обновление существующей записи данных.
     * 
     * @param deviceDataDto новые данные для обновления
     * @param id идентификатор обновляемой записи
     * @return статус операции (SUCCESS/ERROR)
     */
    Status update(DeviceDataDto deviceDataDto, Long id);
    
    /**
     * Удаление записи данных по идентификатору.
     * 
     * @param id идентификатор удаляемой записи
     * @return статус операции (SUCCESS/ERROR)
     */
    Status delete(Long id);
    
    // Методы поиска и фильтрации
    
    /**
     * Получение всех данных для указанного устройства.
     * Данные возвращаются отсортированными по времени (новые первые).
     * 
     * @param deviceId идентификатор устройства
     * @return список данных устройства
     */
    List<DeviceDataDto> findByDeviceId(Long deviceId);
    
    /**
     * Получение данных за указанный временной диапазон.
     * 
     * @param start начальная дата и время диапазона
     * @param end конечная дата и время диапазона
     * @return список данных за указанный период
     */
    List<DeviceDataDto> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Получение последней записи данных для указанного устройства.
     * 
     * @param deviceId идентификатор устройства
     * @return последняя запись данных устройства или null, если данных нет
     */
    DeviceDataDto findLatestByDeviceId(Long deviceId);
    
    // Расширенные методы с пагинацией
    
    /**
     * Получение данных устройства с поддержкой пагинации.
     * 
     * @param deviceId идентификатор устройства
     * @param pageable параметры пагинации (номер страницы, размер, сортировка)
     * @return страница данных устройства
     */
    Page<DeviceDataDto> findByDeviceId(Long deviceId, Pageable pageable);
    
    /**
     * Получение данных за временной диапазон с поддержкой пагинации.
     * 
     * @param start начальная дата диапазона
     * @param end конечная дата диапазона
     * @param pageable параметры пагинации
     * @return страница данных за указанный период
     */
    Page<DeviceDataDto> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    /**
     * Получение данных устройства за период с пагинацией.
     * 
     * @param deviceId идентификатор устройства
     * @param start начальная дата диапазона
     * @param end конечная дата диапазона
     * @param pageable параметры пагинации
     * @return страница данных устройства за период
     */
    Page<DeviceDataDto> findByDeviceIdAndTimestampBetween(
        Long deviceId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    /**
     * Получение последних N записей данных для устройства.
     * 
     * @param deviceId идентификатор устройства
     * @param limit количество последних записей (максимум)
     * @return список последних записей данных
     */
    List<DeviceDataDto> findLatestDataByDeviceId(Long deviceId, int limit);
    
    // Методы фильтрации по значениям сенсоров
    
    /**
     * Поиск данных с температурой в указанном диапазоне.
     * 
     * @param minTemp минимальная температура
     * @param maxTemp максимальная температура
     * @return список данных с температурой в диапазоне
     */
    List<DeviceDataDto> findByTemperatureBetween(Float minTemp, Float maxTemp);
    
    /**
     * Поиск данных с влажностью в указанном диапазоне.
     * 
     * @param minHumidity минимальная влажность
     * @param maxHumidity максимальная влажность
     * @return список данных с влажностью в диапазоне
     */
    List<DeviceDataDto> findByHumidityBetween(Float minHumidity, Float maxHumidity);
    
    /**
     * Поиск данных с экстремальными значениями температуры.
     * 
     * @param threshold пороговое значение температуры
     * @param isHigh true для поиска высоких температур, false для низких
     * @return список данных с экстремальными температурами
     */
    List<DeviceDataDto> findExtremeTemperatureData(Float threshold, boolean isHigh);
    
    /**
     * Поиск данных с экстремальными значениями влажности.
     * 
     * @param threshold пороговое значение влажности
     * @param isHigh true для поиска высокой влажности, false для низкой
     * @return список данных с экстремальной влажностью
     */
    List<DeviceDataDto> findExtremeHumidityData(Float threshold, boolean isHigh);
    
    // Комплексный поиск и фильтрация
    
    /**
     * Комплексный поиск данных с множественными фильтрами.
     * 
     * @param deviceId идентификатор устройства (может быть null)
     * @param timestampAfter данные после указанной даты (может быть null)
     * @param timestampBefore данные до указанной даты (может быть null)
     * @param minTemp минимальная температура (может быть null)
     * @param maxTemp максимальная температура (может быть null)
     * @param minHumidity минимальная влажность (может быть null)
     * @param maxHumidity максимальная влажность (может быть null)
     * @param pageable параметры пагинации
     * @return страница данных, соответствующих критериям
     */
    Page<DeviceDataDto> findDataWithFilters(
        Long deviceId,
        LocalDateTime timestampAfter,
        LocalDateTime timestampBefore,
        Float minTemp,
        Float maxTemp,
        Float minHumidity,
        Float maxHumidity,
        Pageable pageable
    );
    
    // Аналитические и статистические методы
    
    /**
     * Вычисление средней температуры для устройства за период.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой вычисляется среднее
     * @return средняя температура или null, если данных нет
     */
    Double getAverageTemperature(Long deviceId, LocalDateTime since);
    
    /**
     * Вычисление средней влажности для устройства за период.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой вычисляется среднее
     * @return средняя влажность или null, если данных нет
     */
    Double getAverageHumidity(Long deviceId, LocalDateTime since);
    
    /**
     * Получение диапазона температур (минимум и максимум) за период.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой ищется диапазон
     * @return Map с ключами "min" и "max", содержащий границы диапазона
     */
    Map<String, Float> getTemperatureRange(Long deviceId, LocalDateTime since);
    
    /**
     * Получение диапазона влажности (минимум и максимум) за период.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой ищется диапазон
     * @return Map с ключами "min" и "max", содержащий границы диапазона
     */
    Map<String, Float> getHumidityRange(Long deviceId, LocalDateTime since);
    
    // Методы временной агрегации
    
    /**
     * Получение почасовых средних значений температуры и влажности.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой группируются данные
     * @return список Map с ключами: hour, avgTemp, avgHumidity, dataCount
     */
    List<Map<String, Object>> getHourlyAverages(Long deviceId, LocalDateTime since);
    
    /**
     * Получение дневной статистики с полными агрегатами.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой группируются данные
     * @return список Map с ключами: day, avgTemp, minTemp, maxTemp, avgHumidity, minHumidity, maxHumidity, dataCount
     */
    List<Map<String, Object>> getDailyStatistics(Long deviceId, LocalDateTime since);
    
    // Методы обнаружения аномалий
    
    /**
     * Обнаружение аномалий в показаниях сенсоров устройства.
     * 
     * @param deviceId идентификатор устройства
     * @param tempThresholdLow нижний порог нормальной температуры
     * @param tempThresholdHigh верхний порог нормальной температуры
     * @param humidityThresholdLow нижний порог нормальной влажности
     * @param humidityThresholdHigh верхний порог нормальной влажности
     * @param since дата, начиная с которой ищутся аномалии
     * @return список данных с аномальными показаниями
     */
    List<DeviceDataDto> detectAnomalies(
        Long deviceId,
        Float tempThresholdLow,
        Float tempThresholdHigh,
        Float humidityThresholdLow,
        Float humidityThresholdHigh,
        LocalDateTime since
    );
    
    /**
     * Получение последних данных для множества устройств.
     * Полезно для дашбордов и мониторинга.
     * 
     * @param deviceIds список идентификаторов устройств
     * @param since дата, после которой ищутся данные
     * @return список последних данных для каждого устройства
     */
    List<DeviceDataDto> getLatestDataForDevices(List<Long> deviceIds, LocalDateTime since);
    
    // Статистические методы
    
    /**
     * Подсчет общего количества записей данных для устройства.
     * 
     * @param deviceId идентификатор устройства
     * @return общее количество записей данных
     */
    long countDataByDevice(Long deviceId);
    
    /**
     * Подсчет количества записей данных за период.
     * 
     * @param deviceId идентификатор устройства
     * @param after дата, после которой считаются записи
     * @return количество записей данных после указанной даты
     */
    long countDataByDeviceAfter(Long deviceId, LocalDateTime after);
    
    /**
     * Подсчет общего количества записей в системе за период.
     * 
     * @param start начальная дата диапазона
     * @param end конечная дата диапазона
     * @return количество записей в указанном диапазоне
     */
    long countDataBetween(LocalDateTime start, LocalDateTime end);
    
    // Методы проверки и очистки данных
    
    /**
     * Проверка наличия данных для устройства.
     * 
     * @param deviceId идентификатор устройства
     * @return true, если у устройства есть хотя бы одна запись данных
     */
    boolean hasDataForDevice(Long deviceId);
    
    /**
     * Проверка наличия недавних данных для устройства.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, после которой проверяется наличие данных
     * @return true, если у устройства есть данные после указанной даты
     */
    boolean hasRecentDataForDevice(Long deviceId, LocalDateTime since);
    
    /**
     * Удаление старых записей данных для оптимизации базы данных.
     * 
     * @param before дата, до которой удаляются все записи
     * @return количество удаленных записей
     */
    long deleteOldData(LocalDateTime before);
    
    /**
     * Удаление старых записей конкретного устройства.
     * 
     * @param deviceId идентификатор устройства
     * @param before дата, до которой удаляются записи
     * @return количество удаленных записей
     */
    long deleteOldDataForDevice(Long deviceId, LocalDateTime before);
} 