package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDataDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с данными температуры и влажности
 */
public interface TempAndHumidityDataService {
    
    /**
     * Создание новой записи данных температуры и влажности
     * @param tempAndHumidityDataDto данные температуры и влажности
     * @return созданная запись
     */
    TempAndHumidityDataDto createTempAndHumidityData(TempAndHumidityDataDto tempAndHumidityDataDto);
    
    /**
     * Получение записи данных по идентификатору
     * @param id идентификатор записи
     * @return запись данных или пустой Optional
     */
    Optional<TempAndHumidityDataDto> getTempAndHumidityDataById(Long id);
    
    /**
     * Обновление записи данных температуры и влажности
     * @param id идентификатор записи
     * @param tempAndHumidityDataDto новые данные
     * @return обновленная запись
     */
    Optional<TempAndHumidityDataDto> updateTempAndHumidityData(Long id, TempAndHumidityDataDto tempAndHumidityDataDto);
    
    /**
     * Удаление записи данных
     * @param id идентификатор записи
     * @return true если запись удалена, false если не найдена
     */
    boolean deleteTempAndHumidityData(Long id);
    
    /**
     * Получение всех данных температуры и влажности
     * @return список всех записей
     */
    List<TempAndHumidityDataDto> getAllTempAndHumidityData();
    
    /**
     * Получение данных по устройству
     * @param deviceId идентификатор устройства
     * @return список данных для устройства
     */
    List<TempAndHumidityDataDto> getTempAndHumidityDataByDevice(Long deviceId);
    
    /**
     * Получение данных по устройству отсортированных по времени
     * @param deviceId идентификатор устройства
     * @return список данных отсортированный по времени
     */
    List<TempAndHumidityDataDto> getTempAndHumidityDataByDeviceOrderByTime(Long deviceId);
    
    /**
     * Получение последних данных для устройства
     * @param deviceId идентификатор устройства
     * @return последние данные или пустой Optional
     */
    Optional<TempAndHumidityDataDto> getLatestTempAndHumidityDataByDevice(Long deviceId);
    
    /**
     * Получение данных в заданном временном диапазоне
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных в указанном диапазоне
     */
    List<TempAndHumidityDataDto> getTempAndHumidityDataByDeviceAndTimeRange(Long deviceId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Получение данных с температурой больше указанного значения
     * @param temperature минимальная температура
     * @return список данных
     */
    List<TempAndHumidityDataDto> getTempAndHumidityDataByTemperatureGreaterThan(Float temperature);
    
    /**
     * Получение данных с влажностью больше указанного значения
     * @param humidity минимальная влажность
     * @return список данных
     */
    List<TempAndHumidityDataDto> getTempAndHumidityDataByHumidityGreaterThan(Float humidity);
} 