package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.LedStripDataDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с данными LED ленты
 */
public interface LedStripDataService {
    
    /**
     * Создание новой записи данных LED ленты
     * @param ledStripDataDto данные LED ленты
     * @return созданная запись
     */
    LedStripDataDto createLedStripData(LedStripDataDto ledStripDataDto);
    
    /**
     * Получение записи данных LED ленты по идентификатору
     * @param id идентификатор записи
     * @return запись данных или пустой Optional
     */
    Optional<LedStripDataDto> getLedStripDataById(Long id);
    
    /**
     * Обновление записи данных LED ленты
     * @param id идентификатор записи
     * @param ledStripDataDto новые данные
     * @return обновленная запись
     */
    Optional<LedStripDataDto> updateLedStripData(Long id, LedStripDataDto ledStripDataDto);
    
    /**
     * Удаление записи данных LED ленты
     * @param id идентификатор записи
     * @return true если запись удалена, false если не найдена
     */
    boolean deleteLedStripData(Long id);
    
    /**
     * Получение всех данных LED ленты
     * @return список всех записей
     */
    List<LedStripDataDto> getAllLedStripData();
    
    /**
     * Получение данных LED ленты по устройству
     * @param deviceId идентификатор устройства
     * @return список данных для устройства
     */
    List<LedStripDataDto> getLedStripDataByDevice(Long deviceId);
    
    /**
     * Получение данных LED ленты по устройству отсортированных по времени
     * @param deviceId идентификатор устройства
     * @return список данных отсортированный по времени
     */
    List<LedStripDataDto> getLedStripDataByDeviceOrderByTime(Long deviceId);
    
    /**
     * Получение последних данных LED ленты для устройства
     * @param deviceId идентификатор устройства
     * @return последние данные или пустой Optional
     */
    Optional<LedStripDataDto> getLatestLedStripDataByDevice(Long deviceId);
    
    /**
     * Получение данных LED ленты в заданном временном диапазоне
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных в указанном диапазоне
     */
    List<LedStripDataDto> getLedStripDataByDeviceAndTimeRange(Long deviceId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Получение данных LED ленты с яркостью больше указанного значения
     * @param brightness минимальная яркость
     * @return список данных
     */
    List<LedStripDataDto> getLedStripDataByBrightnessGreaterThan(Integer brightness);
} 