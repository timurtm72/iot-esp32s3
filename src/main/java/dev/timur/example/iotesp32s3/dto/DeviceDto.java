package dev.timur.example.iotesp32s3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import dev.timur.example.iotesp32s3.enums.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
    @NotBlank
    @Size(max = 255)
    private String name;
    
    /** Описание устройства */
    private String description;
    
    /** Местоположение устройства */
    private LocationDto location;

    /** Параметры WiFi устройства */
    private WiFiParametersDto wifiParameters;

    /** Статус устройства */
    private DeviceStatus status;
    
    /** Идентификатор владельца устройства */
    private Long ownerId;
    
    /** Время создания записи */
    private LocalDateTime createdAt;
    
    /** Время последнего изменения записи */
    private LocalDateTime modifiedAt;
} 