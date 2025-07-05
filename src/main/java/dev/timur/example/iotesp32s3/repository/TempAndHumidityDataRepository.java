package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.TempAndHumidityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с данными температуры и влажности
 */
@Repository
public interface TempAndHumidityDataRepository extends JpaRepository<TempAndHumidityData, Long> {
    
    /**
     * Поиск данных температуры и влажности по устройству
     * @param deviceId идентификатор устройства
     * @return список данных для устройства
     */
    List<TempAndHumidityData> findByDeviceId(Long deviceId);
    
    /**
     * Поиск данных по устройству с сортировкой по времени (по убыванию)
     * @param deviceId идентификатор устройства
     * @return список данных отсортированный по времени
     */
    List<TempAndHumidityData> findByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    /**
     * Поиск последних данных температуры и влажности для устройства
     * @param deviceId идентификатор устройства
     * @return последние данные
     */
    Optional<TempAndHumidityData> findFirstByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    /**
     * Поиск данных по устройству в заданном временном диапазоне
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных в указанном диапазоне
     */
    List<TempAndHumidityData> findByDeviceIdAndTimestampBetween(Long deviceId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Поиск данных с температурой больше указанного значения
     * @param temperature минимальная температура
     * @return список данных
     */
    List<TempAndHumidityData> findByTemperatureGreaterThan(Float temperature);
    
    /**
     * Поиск данных с влажностью больше указанного значения
     * @param humidity минимальная влажность
     * @return список данных
     */
    List<TempAndHumidityData> findByHumidityGreaterThan(Float humidity);

    /**
     * Удалить данные старше указанного времени
     * @param threshold временной порог
     */
    void deleteByTimestampBefore(LocalDateTime threshold);
} 