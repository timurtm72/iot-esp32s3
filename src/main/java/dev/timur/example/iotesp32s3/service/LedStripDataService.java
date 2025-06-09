package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.LedStripDataDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с данными LED ленты
 */
public interface LedStripDataService {
    
    /**
     * Создать новую запись данных
     * @param dataDto данные LED ленты
     * @return созданная запись
     */
    LedStripDataDto createData(LedStripDataDto dataDto);
    
    /**
     * Получить запись по ID
     * @param id идентификатор записи
     * @return запись данных
     */
    LedStripDataDto getDataById(Long id);
    
    /**
     * Получить все данные устройства
     * @param deviceId идентификатор устройства
     * @return список данных
     */
    List<LedStripDataDto> getDataByDeviceId(Long deviceId);
    
    /**
     * Получить данные за период
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных за период
     */
    List<LedStripDataDto> getDataByPeriod(Long deviceId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Получить последние данные устройства
     * @param deviceId идентификатор устройства
     * @param limit количество записей
     * @return последние записи
     */
    List<LedStripDataDto> getLatestData(Long deviceId, int limit);
    
    /**
     * Получить текущее состояние LED ленты
     * @param deviceId идентификатор устройства
     * @return текущее состояние
     */
    LedStripDataDto getCurrentState(Long deviceId);
    
    /**
     * Обновить запись данных
     * @param id идентификатор записи
     * @param dataDto новые данные
     * @return обновленная запись
     */
    LedStripDataDto updateData(Long id, LedStripDataDto dataDto);
    
    /**
     * Удалить запись данных
     * @param id идентификатор записи
     */
    void deleteData(Long id);
    
    /**
     * Получить данные по цвету
     * @param deviceId идентификатор устройства
     * @param redColor красный цвет
     * @param greenColor зеленый цвет
     * @param blueColor синий цвет
     * @return записи с указанным цветом
     */
    List<LedStripDataDto> getDataByColor(Long deviceId, Integer redColor, Integer greenColor, Integer blueColor);
} 