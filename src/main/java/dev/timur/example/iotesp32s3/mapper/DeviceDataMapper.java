package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.DeviceDataDto;
import dev.timur.example.iotesp32s3.model.DeviceData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct маппер для преобразования между моделью DeviceData и соответствующими DTO.
 * Специализирован для работы с данными телеметрии IoT устройств и временными рядами.
 * 
 * Особенности:
 * - Конфигурирован как Spring компонент для автоматического внедрения
 * - Обрабатывает связь с устройством через deviceId вместо полного объекта Device
 * - Оптимизирован для массовых операций с данными телеметрии
 * - Поддерживает все типы сенсорных данных: температура, влажность, RGB, яркость
 * - Автоматическое преобразование временных меток (timestamp)
 */
@Mapper(componentModel = "spring")
public interface DeviceDataMapper {
    
    /**
     * Преобразует модель данных устройства в DTO для передачи данных.
     * Извлекает ID устройства из связанного объекта Device для упрощения структуры DTO.
     * 
     * @param deviceData модель данных устройства из базы данных
     * @return DeviceDataDto с данными телеметрии и ID устройства
     */
    @Mapping(target = "deviceId", source = "device.id")
    DeviceDataDto toDto(DeviceData deviceData);
    
    /**
     * Преобразует DTO данных устройства в модель для сохранения в базе данных.
     * Игнорирует поле device - оно должно быть установлено отдельно в сервисном слое
     * для правильного связывания с устройством.
     * 
     * @param deviceDataDto DTO с данными телеметрии
     * @return модель DeviceData для работы с базой данных (без связанного Device)
     */
    @Mapping(target = "device", ignore = true)
    DeviceData toEntity(DeviceDataDto deviceDataDto);
} 