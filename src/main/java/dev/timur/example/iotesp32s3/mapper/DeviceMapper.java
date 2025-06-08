package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.model.Device;
import org.mapstruct.Mapper;

/**
 * MapStruct маппер для преобразования между моделью Device и соответствующими DTO.
 * Обеспечивает безопасное и эффективное преобразование данных IoT устройств.
 * 
 * Особенности:
 * - Конфигурирован как Spring компонент для автоматического внедрения
 * - Поддерживает маппинг всех полей устройства включая метаданные
 * - Автоматическое преобразование дат и времени
 * - Compile-time генерация кода для максимальной производительности
 * - Поддержка концепции мягкого удаления через removedAt поле
 */
@Mapper(componentModel = "spring")
public interface DeviceMapper {
    
    /**
     * Преобразует модель устройства в DTO для передачи данных.
     * Включает все поля устройства: идентификатор, название, местоположение,
     * данные о создании, обновлении и мягком удалении.
     * 
     * @param device модель устройства из базы данных
     * @return DeviceDto с полными данными устройства
     */
    DeviceDto toDto(Device device);
    
    /**
     * Преобразует DTO устройства в модель для сохранения в базе данных.
     * Автоматически маппит все поля с проверкой типов на этапе компиляции.
     * 
     * @param deviceDto DTO с данными устройства
     * @return модель Device для работы с базой данных
     */
    Device toEntity(DeviceDto deviceDto);
} 