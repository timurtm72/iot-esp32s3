package dev.timur.example.iotesp32s3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для представления данных IoT устройства
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {
    
    /** Уникальный идентификатор устройства */
    private Long id;
    
    /** Название устройства */
    private String name;
    
    /** Описание устройства */
    private String description;
    
    /** Местоположение устройства */
    private String location;
    
    /** Идентификатор владельца устройства */
    private Long ownerId;
    
    /** Время создания записи */
    private LocalDateTime createdAt;
    
    /** Время последнего изменения записи */
    private LocalDateTime modifiedAt;
} 