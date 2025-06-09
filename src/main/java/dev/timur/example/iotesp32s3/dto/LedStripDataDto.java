package dev.timur.example.iotesp32s3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для представления данных управления LED лентой
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LedStripDataDto {
    
    /** Уникальный идентификатор записи данных */
    private Long id;
    
    /** Значение красного цвета (0-255) */
    private Integer redColor;
    
    /** Значение зеленого цвета (0-255) */
    private Integer greenColor;
    
    /** Значение синего цвета (0-255) */
    private Integer blueColor;
    
    /** Яркость LED ленты (0-255) */
    private Integer brightness;
    
    /** Время установки значений */
    private LocalDateTime timestamp;
    
    /** Идентификатор связанного устройства */
    private Long deviceId;
} 