package dev.timur.example.iotesp32s3.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO для передачи данных IoT устройства в системе ESP32S3.
 * Используется для операций создания, чтения, изменения и удаления устройств.
 * 
 * Поддерживает:
 * - Основную информацию об устройстве (название, описание, местоположение)
 * - Временные метки для аудита изменений
 * - Концепцию мягкого удаления (через removedAt в модели)
 * - Интеграцию с системой сбора телеметрии
 * 
 * Особенности:
 * - Оптимизирован для REST API и JSON сериализации
 * - Поддерживает Lombok для генерации кода
 * - Совместим с MapStruct маппингом
 * - Включает Builder паттерн для удобства создания объектов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDto {
    
    /** Уникальный идентификатор устройства */
    private Long id;
    
    /** Название устройства (например, "ESP32-Sensor-01") */
    private String name;
    
    /** Описание устройства и его назначения */
    private String description;
    
    /** Физическое местоположение устройства (например, "Комната 101") */
    private String location;
    
    /** Дата и время создания записи об устройстве */
    private LocalDateTime createdAt;
    
    /** Дата и время последнего изменения данных устройства */
    private LocalDateTime modifiedAt;
    
    /** Идентификатор владельца устройства */
    private Long ownerId;
} 