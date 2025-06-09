package dev.timur.example.iotesp32s3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для представления данных с датчиков температуры и влажности
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempAndHumidityDataDto {
    
    /** Уникальный идентификатор записи данных */
    private Long id;
    
    /** Значение влажности в процентах */
    private Float humidity;
    
    /** Значение температуры в градусах Цельсия */
    private Float temperature;
    
    /** Время снятия показаний с датчиков */
    private LocalDateTime timestamp;
    
    /** Идентификатор связанного устройства */
    private Long deviceId;
} 