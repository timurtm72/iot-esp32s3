package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.DeviceData;
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
 * Репозиторий для управления данными IoT устройств в системе.
 * Предоставляет современные методы для сохранения, поиска и анализа телеметрии
 * с поддержкой временных диапазонов, агрегации и статистических вычислений.
 * Оптимизирован для работы с большими объемами временных данных (time-series data).
 */
@Repository
public interface DeviceDataRepository extends JpaRepository<DeviceData, Long>, JpaSpecificationExecutor<DeviceData> {
    
    // Query Methods - заменяем @Query на автогенерацию
    
    /**
     * Поиск записи данных устройства по идентификатору.
     * 
     * @param id уникальный идентификатор записи данных
     * @return Optional содержащий данные устройства, если найдены
     */
    Optional<DeviceData> findById(Long id);
    
    /**
     * Получение всех данных для указанного устройства, отсортированных по времени (новые первые).
     * 
     * @param deviceId идентификатор устройства
     * @return список данных устройства, отсортированный по убыванию времени
     */
    List<DeviceData> findByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    /**
     * Получение данных устройства с поддержкой пагинации, отсортированных по времени (новые первые).
     * 
     * @param deviceId идентификатор устройства
     * @param pageable параметры пагинации (номер страницы, размер страницы, сортировка)
     * @return страница данных устройства
     */
    Page<DeviceData> findByDeviceIdOrderByTimestampDesc(Long deviceId, Pageable pageable);
    
    /**
     * Получение всех данных за указанный временной диапазон, отсортированных по времени (новые первые).
     * 
     * @param start начальная дата и время диапазона
     * @param end конечная дата и время диапазона
     * @return список данных за указанный период
     */
    List<DeviceData> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);
    
    /**
     * Получение данных за временной диапазон с поддержкой пагинации.
     * 
     * @param start начальная дата и время диапазона
     * @param end конечная дата и время диапазона
     * @param pageable параметры пагинации
     * @return страница данных за указанный период
     */
    Page<DeviceData> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Последние данные устройства
    
    /**
     * Получение самой последней записи данных для указанного устройства.
     * 
     * @param deviceId идентификатор устройства
     * @return Optional содержащий последнюю запись данных устройства
     */
    Optional<DeviceData> findFirstByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    /**
     * Получение последних 10 записей данных для указанного устройства.
     * 
     * @param deviceId идентификатор устройства
     * @return список из максимум 10 последних записей данных
     */
    List<DeviceData> findTop10ByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    // Данные за период для устройства
    
    /**
     * Получение данных устройства после указанной даты, отсортированных по времени (новые первые).
     * 
     * @param deviceId идентификатор устройства
     * @param after дата и время, после которой нужны данные
     * @return список данных устройства после указанной даты
     */
    List<DeviceData> findByDeviceIdAndTimestampAfterOrderByTimestampDesc(Long deviceId, LocalDateTime after);
    
    /**
     * Получение данных устройства за указанный временной диапазон.
     * 
     * @param deviceId идентификатор устройства
     * @param start начальная дата и время диапазона
     * @param end конечная дата и время диапазона
     * @return список данных устройства за указанный период
     */
    List<DeviceData> findByDeviceIdAndTimestampBetweenOrderByTimestampDesc(
        Long deviceId, LocalDateTime start, LocalDateTime end);
    
    /**
     * Получение данных устройства за временной диапазон с поддержкой пагинации.
     * 
     * @param deviceId идентификатор устройства
     * @param start начальная дата и время диапазона
     * @param end конечная дата и время диапазона
     * @param pageable параметры пагинации
     * @return страница данных устройства за указанный период
     */
    Page<DeviceData> findByDeviceIdAndTimestampBetweenOrderByTimestampDesc(
        Long deviceId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Фильтрация по диапазонам значений
    
    /**
     * Поиск данных с температурой в указанном диапазоне.
     * 
     * @param minTemp минимальная температура
     * @param maxTemp максимальная температура
     * @return список данных с температурой в указанном диапазоне
     */
    List<DeviceData> findByTemperatureBetweenOrderByTimestampDesc(Float minTemp, Float maxTemp);
    
    /**
     * Поиск данных с влажностью в указанном диапазоне.
     * 
     * @param minHumidity минимальная влажность
     * @param maxHumidity максимальная влажность
     * @return список данных с влажностью в указанном диапазоне
     */
    List<DeviceData> findByHumidityBetweenOrderByTimestampDesc(Float minHumidity, Float maxHumidity);
    
    /**
     * Поиск данных конкретного устройства с температурой в указанном диапазоне.
     * 
     * @param deviceId идентификатор устройства
     * @param minTemp минимальная температура
     * @param maxTemp максимальная температура
     * @return список данных устройства с температурой в диапазоне
     */
    List<DeviceData> findByDeviceIdAndTemperatureBetweenOrderByTimestampDesc(
        Long deviceId, Float minTemp, Float maxTemp);
    
    /**
     * Поиск данных конкретного устройства с влажностью в указанном диапазоне.
     * 
     * @param deviceId идентификатор устройства
     * @param minHumidity минимальная влажность
     * @param maxHumidity максимальная влажность
     * @return список данных устройства с влажностью в диапазоне
     */
    List<DeviceData> findByDeviceIdAndHumidityBetweenOrderByTimestampDesc(
        Long deviceId, Float minHumidity, Float maxHumidity);
    
    // Экстремальные значения
    
    /**
     * Поиск данных с температурой выше указанного значения.
     * 
     * @param temperature пороговое значение температуры
     * @return список данных с температурой выше указанной
     */
    List<DeviceData> findByTemperatureGreaterThanOrderByTimestampDesc(Float temperature);
    
    /**
     * Поиск данных с температурой ниже указанного значения.
     * 
     * @param temperature пороговое значение температуры
     * @return список данных с температурой ниже указанной
     */
    List<DeviceData> findByTemperatureLessThanOrderByTimestampDesc(Float temperature);
    
    /**
     * Поиск данных с влажностью выше указанного значения.
     * 
     * @param humidity пороговое значение влажности
     * @return список данных с влажностью выше указанной
     */
    List<DeviceData> findByHumidityGreaterThanOrderByTimestampDesc(Float humidity);
    
    /**
     * Поиск данных с влажностью ниже указанного значения.
     * 
     * @param humidity пороговое значение влажности
     * @return список данных с влажностью ниже указанной
     */
    List<DeviceData> findByHumidityLessThanOrderByTimestampDesc(Float humidity);
    
    /**
     * Оптимизированное получение последних данных для множества устройств.
     * Использует оконную функцию ROW_NUMBER() для эффективного выбора последних записей.
     * 
     * @param deviceIds список идентификаторов устройств
     * @param since дата, после которой нужны данные
     * @return список последних данных для каждого устройства
     */
    @Query(value = """
        SELECT 
            dd.*,
            ROW_NUMBER() OVER (PARTITION BY dd.device_id ORDER BY dd.timestamp DESC) as rn
        FROM bit_device_data dd 
        WHERE dd.device_id IN :deviceIds 
        AND dd.timestamp >= :since
        ORDER BY dd.device_id, dd.timestamp DESC
        """, nativeQuery = true)
    List<DeviceData> findLatestDataForDevices(@Param("deviceIds") List<Long> deviceIds, @Param("since") LocalDateTime since);
    
    // Статистические запросы
    
    /**
     * Вычисление средней температуры для устройства за указанный период.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой вычисляется среднее
     * @return средняя температура или null, если данных нет
     */
    @Query("SELECT AVG(dd.temperature) FROM DeviceData dd WHERE dd.device.id = :deviceId AND dd.timestamp >= :since")
    Double getAverageTemperature(@Param("deviceId") Long deviceId, @Param("since") LocalDateTime since);
    
    /**
     * Вычисление средней влажности для устройства за указанный период.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой вычисляется среднее
     * @return средняя влажность или null, если данных нет
     */
    @Query("SELECT AVG(dd.humidity) FROM DeviceData dd WHERE dd.device.id = :deviceId AND dd.timestamp >= :since")
    Double getAverageHumidity(@Param("deviceId") Long deviceId, @Param("since") LocalDateTime since);
    
    /**
     * Получение диапазона температур (минимум и максимум) для устройства за период.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой ищется диапазон
     * @return массив из двух элементов: [минимальная температура, максимальная температура]
     */
    @Query("SELECT MIN(dd.temperature), MAX(dd.temperature) FROM DeviceData dd WHERE dd.device.id = :deviceId AND dd.timestamp >= :since")
    Object[] getTemperatureRange(@Param("deviceId") Long deviceId, @Param("since") LocalDateTime since);
    
    /**
     * Получение диапазона влажности (минимум и максимум) для устройства за период.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой ищется диапазон
     * @return массив из двух элементов: [минимальная влажность, максимальная влажность]
     */
    @Query("SELECT MIN(dd.humidity), MAX(dd.humidity) FROM DeviceData dd WHERE dd.device.id = :deviceId AND dd.timestamp >= :since")
    Object[] getHumidityRange(@Param("deviceId") Long deviceId, @Param("since") LocalDateTime since);
    
    // Группировка по времени (PostgreSQL specific)
    
    /**
     * Получение почасовых средних значений температуры и влажности.
     * Использует PostgreSQL функцию DATE_TRUNC для группировки по часам.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой группируются данные
     * @return список массивов: [час, средняя температура, средняя влажность, количество записей]
     */
    @Query(value = """
        SELECT 
            DATE_TRUNC('hour', dd.timestamp) as hour,
            AVG(dd.temperature) as avg_temp,
            AVG(dd.humidity) as avg_humidity,
            COUNT(*) as data_count
        FROM bit_device_data dd 
        WHERE dd.device_id = :deviceId 
        AND dd.timestamp >= :since
        GROUP BY DATE_TRUNC('hour', dd.timestamp)
        ORDER BY hour DESC
        """, nativeQuery = true)
    List<Object[]> getHourlyAverages(@Param("deviceId") Long deviceId, @Param("since") LocalDateTime since);
    
    /**
     * Получение дневной статистики с полными агрегатами (мин, макс, среднее).
     * Группирует данные по дням с полной статистикой по температуре и влажности.
     * 
     * @param deviceId идентификатор устройства
     * @param since дата, начиная с которой группируются данные
     * @return список массивов: [день, средняя_темп, мин_темп, макс_темп, средняя_влаж, мин_влаж, макс_влаж, количество]
     */
    @Query(value = """
        SELECT 
            DATE_TRUNC('day', dd.timestamp) as day,
            AVG(dd.temperature) as avg_temp,
            MIN(dd.temperature) as min_temp,
            MAX(dd.temperature) as max_temp,
            AVG(dd.humidity) as avg_humidity,
            MIN(dd.humidity) as min_humidity,
            MAX(dd.humidity) as max_humidity,
            COUNT(*) as data_count
        FROM bit_device_data dd 
        WHERE dd.device_id = :deviceId 
        AND dd.timestamp >= :since
        GROUP BY DATE_TRUNC('day', dd.timestamp)
        ORDER BY day DESC
        """, nativeQuery = true)
    List<Object[]> getDailyStatistics(@Param("deviceId") Long deviceId, @Param("since") LocalDateTime since);
    
    // Подсчеты
    
    /**
     * Подсчет общего количества записей данных для устройства.
     * 
     * @param deviceId идентификатор устройства
     * @return общее количество записей данных устройства
     */
    long countByDeviceId(Long deviceId);
    
    /**
     * Подсчет количества записей данных устройства после указанной даты.
     * 
     * @param deviceId идентификатор устройства
     * @param after дата, после которой считаются записи
     * @return количество записей данных после указанной даты
     */
    long countByDeviceIdAndTimestampAfter(Long deviceId, LocalDateTime after);
    
    /**
     * Подсчет количества записей данных в указанном временном диапазоне.
     * 
     * @param start начальная дата диапазона
     * @param end конечная дата диапазона
     * @return количество записей в указанном диапазоне
     */
    long countByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    // Exists методы
    
    /**
     * Проверка наличия данных для указанного устройства.
     * 
     * @param deviceId идентификатор устройства
     * @return true, если у устройства есть хотя бы одна запись данных
     */
    boolean existsByDeviceId(Long deviceId);
    
    /**
     * Проверка наличия данных для устройства после указанной даты.
     * 
     * @param deviceId идентификатор устройства
     * @param after дата, после которой проверяется наличие данных
     * @return true, если у устройства есть данные после указанной даты
     */
    boolean existsByDeviceIdAndTimestampAfter(Long deviceId, LocalDateTime after);
    
    // Удаление старых данных
    
    /**
     * Удаление всех записей данных старше указанной даты.
     * Используется для очистки старых данных и оптимизации базы данных.
     * 
     * @param before дата, до которой удаляются все записи
     */
    void deleteByTimestampBefore(LocalDateTime before);
    
    /**
     * Удаление старых записей данных конкретного устройства.
     * 
     * @param deviceId идентификатор устройства
     * @param before дата, до которой удаляются записи устройства
     */
    void deleteByDeviceIdAndTimestampBefore(Long deviceId, LocalDateTime before);
} 