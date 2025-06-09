package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.TempAndHumidityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с данными датчиков температуры и влажности
 */
@Repository
public interface TempAndHumidityDataRepository extends JpaRepository<TempAndHumidityData, Long> {
    
    /**
     * Найти все данные по устройству
     * @param deviceId идентификатор устройства
     * @return список данных
     */
    List<TempAndHumidityData> findByDeviceId(Long deviceId);
    
    /**
     * Найти данные по устройству за период
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных за период
     */
    List<TempAndHumidityData> findByDeviceIdAndTimestampBetweenOrderByTimestampDesc(Long deviceId, 
                                                                                   LocalDateTime startTime, 
                                                                                   LocalDateTime endTime);
    
    /**
     * Найти записи по устройству, отсортированные по времени (последние сначала)
     * @param deviceId идентификатор устройства
     * @return записи, отсортированные по времени
     */
    List<TempAndHumidityData> findByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    /**
     * Найти записи по температуре выше указанного значения
     * @param deviceId идентификатор устройства
     * @param temperature минимальная температура
     * @return записи с температурой выше указанной
     */
    List<TempAndHumidityData> findByDeviceIdAndTemperatureGreaterThan(Long deviceId, Float temperature);
    
    /**
     * Найти записи по влажности в указанном диапазоне
     * @param deviceId идентификатор устройства
     * @param minHumidity минимальная влажность
     * @param maxHumidity максимальная влажность
     * @return записи с влажностью в диапазоне
     */
    List<TempAndHumidityData> findByDeviceIdAndHumidityBetween(Long deviceId, Float minHumidity, Float maxHumidity);
} 