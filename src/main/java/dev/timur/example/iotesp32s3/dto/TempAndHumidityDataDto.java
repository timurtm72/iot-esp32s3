package dev.timur.example.iotesp32s3.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO для передачи данных с датчиков температуры и влажности
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempAndHumidityDataDto {
    
    /** Уникальный идентификатор записи данных */
    private Long id;

    /** Значение влажности в процентах */
    private Float humidity;

    /** Значение температуры в градусах Цельсия */
    private Float temperature;

    /** Время снятия показаний с датчиков */
    private LocalDateTime timestamp;

    /** Идентификатор устройства */
    private Long deviceId;
} 