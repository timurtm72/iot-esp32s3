package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.DeviceDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с устройствами IoT
 */
public interface DeviceService {
    
    /**
     * Создание нового устройства
     * @param deviceDto данные устройства
     * @return созданное устройство
     */
    DeviceDto createDevice(DeviceDto deviceDto);
    
    /**
     * Получение устройства по идентификатору
     * @param id идентификатор устройства
     * @return устройство или пустой Optional
     */
    Optional<DeviceDto> getDeviceById(Long id);
    
    /**
     * Обновление данных устройства
     * @param id идентификатор устройства
     * @param deviceDto новые данные устройства
     * @return обновленное устройство
     */
    Optional<DeviceDto> updateDevice(Long id, DeviceDto deviceDto);
    
    /**
     * Мягкое удаление устройства
     * @param id идентификатор устройства
     * @return true если устройство удалено, false если не найдено
     */
    boolean deleteDevice(Long id);
    
    /**
     * Получение всех активных устройств
     * @return список активных устройств
     */
    List<DeviceDto> getAllActiveDevices();
    
    /**
     * Поиск устройства по названию
     * @param name название устройства
     * @return устройство или пустой Optional
     */
    Optional<DeviceDto> getDeviceByName(String name);
    
    /**
     * Получение устройств пользователя
     * @param ownerId идентификатор владельца
     * @return список устройств пользователя
     */
    List<DeviceDto> getDevicesByOwner(Long ownerId);
    
    /**
     * Получение устройств по городу
     * @param city город
     * @return список устройств
     */
    List<DeviceDto> getDevicesByCity(String city);
    
    /**
     * Получение устройств созданных после указанной даты
     * @param createdAfter дата создания
     * @return список устройств
     */
    List<DeviceDto> getDevicesCreatedAfter(LocalDateTime createdAfter);
} 