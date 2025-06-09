package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.LedStripDto;

import java.util.List;

/**
 * Сервис для работы с LED лентами
 */
public interface LedStripService {
    
    /**
     * Создать новое устройство LED ленты
     * @param deviceDto данные устройства
     * @return созданное устройство
     */
    LedStripDto createDevice(LedStripDto deviceDto);
    
    /**
     * Получить устройство по ID
     * @param id идентификатор устройства
     * @return устройство
     */
    LedStripDto getDeviceById(Long id);
    
    /**
     * Получить все устройства пользователя
     * @param ownerId идентификатор владельца
     * @return список устройств
     */
    List<LedStripDto> getDevicesByOwnerId(Long ownerId);
    
    /**
     * Получить все активные устройства
     * @return список активных устройств
     */
    List<LedStripDto> getAllActiveDevices();
    
    /**
     * Обновить устройство
     * @param id идентификатор устройства
     * @param deviceDto новые данные устройства
     * @return обновленное устройство
     */
    LedStripDto updateDevice(Long id, LedStripDto deviceDto);
    
    /**
     * Удалить устройство (мягкое удаление)
     * @param id идентификатор устройства
     */
    void deleteDevice(Long id);
    
    /**
     * Проверить существование устройства
     * @param id идентификатор устройства
     * @return true если устройство существует
     */
    boolean existsById(Long id);
} 