package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDataDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с данными датчиков температуры и влажности
 */
public interface TempAndHumidityDataService {
    
    /**
     * Создать новую запись данных
     * @param dataDto данные с датчиков
     * @return созданная запись
     */
    TempAndHumidityDataDto createData(TempAndHumidityDataDto dataDto);
    
    /**
     * Получить запись по ID
     * @param id идентификатор записи
     * @return запись данных
     */
    TempAndHumidityDataDto getDataById(Long id);
    
    /**
     * Получить все данные устройства
     * @param deviceId идентификатор устройства
     * @return список данных
     */
    List<TempAndHumidityDataDto> getDataByDeviceId(Long deviceId);
    
    /**
     * Получить данные за период
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных за период
     */
    List<TempAndHumidityDataDto> getDataByPeriod(Long deviceId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Получить последние данные устройства
     * @param deviceId идентификатор устройства
     * @param limit количество записей
     * @return последние записи
     */
    List<TempAndHumidityDataDto> getLatestData(Long deviceId, int limit);
    
    /**
     * Обновить запись данных
     * @param id идентификатор записи
     * @param dataDto новые данные
     * @return обновленная запись
     */
    TempAndHumidityDataDto updateData(Long id, TempAndHumidityDataDto dataDto);
    
    /**
     * Удалить запись данных
     * @param id идентификатор записи
     */
    void deleteData(Long id);
    
    /**
     * Получить данные с высокой температурой
     * @param deviceId идентификатор устройства
     * @param minTemperature минимальная температура
     * @return записи с температурой выше указанной
     */
    List<TempAndHumidityDataDto> getHighTemperatureData(Long deviceId, Float minTemperature);
} 