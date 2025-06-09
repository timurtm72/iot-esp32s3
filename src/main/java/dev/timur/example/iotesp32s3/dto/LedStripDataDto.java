package dev.timur.example.iotesp32s3.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO для передачи данных управления LED лентой
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    /** Идентификатор устройства */
    private Long deviceId;
} 