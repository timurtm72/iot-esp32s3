package dev.timur.example.iotesp32s3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для представления данных управления LED лентой
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LedStripDataDto {
    
    /** Уникальный идентификатор записи данных */
    private Long id;
    
    @Min(0)
    @Max(255)
    @NotNull
    private Integer redColor;
    
    @Min(0)
    @Max(255)
    @NotNull
    private Integer greenColor;
    
    @Min(0)
    @Max(255)
    @NotNull
    private Integer blueColor;
    
    @Min(0)
    @Max(255)
    @NotNull
    private Integer brightness;
    
    /** Время установки значений */
    private LocalDateTime timestamp;
    
    @NotNull
    private Long deviceId;
} 