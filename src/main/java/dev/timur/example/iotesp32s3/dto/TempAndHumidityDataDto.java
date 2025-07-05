package dev.timur.example.iotesp32s3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

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
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Float humidity;
    
    /** Значение температуры в градусах Цельсия */
    @NotNull
    private Float temperature;
    
    /** Время снятия показаний с датчиков */
    private LocalDateTime timestamp;
    
    /** Идентификатор связанного устройства */
    @NotNull
    private Long deviceId;
} 