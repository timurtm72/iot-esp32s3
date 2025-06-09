package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDto;

import java.util.List;

/**
 * Сервис для работы с устройствами температуры и влажности
 */
public interface TempAndHumidityService {
    
    /**
     * Создать новое устройство температуры и влажности
     * @param deviceDto данные устройства
     * @return созданное устройство
     */
    TempAndHumidityDto createDevice(TempAndHumidityDto deviceDto);
    
    /**
     * Получить устройство по ID
     * @param id идентификатор устройства
     * @return устройство
     */
    TempAndHumidityDto getDeviceById(Long id);
    
    /**
     * Получить все устройства пользователя
     * @param ownerId идентификатор владельца
     * @return список устройств
     */
    List<TempAndHumidityDto> getDevicesByOwnerId(Long ownerId);
    
    /**
     * Получить все активные устройства
     * @return список активных устройств
     */
    List<TempAndHumidityDto> getAllActiveDevices();
    
    /**
     * Обновить устройство
     * @param id идентификатор устройства
     * @param deviceDto новые данные устройства
     * @return обновленное устройство
     */
    TempAndHumidityDto updateDevice(Long id, TempAndHumidityDto deviceDto);
    
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