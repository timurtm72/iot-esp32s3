package dev.timur.example.iotesp32s3.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для передачи данных устройства температуры и влажности
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempAndHumidityDto {
    
    /** Уникальный идентификатор устройства */
    private Long id;
    
    /** Название устройства */
    private String name;
    
    /** Описание устройства */
    private String description;
    
    /** Местоположение устройства */
    private String location;
    
    /** Время создания записи */
    private LocalDateTime createdAt;
    
    /** Время последнего изменения */
    private LocalDateTime modifiedAt;
    
    /** Время удаления записи */
    private LocalDateTime removedAt;
    
    /** Данные с датчиков */
    private List<TempAndHumidityDataDto> dataValues;
    
    /** Идентификатор владельца */
    private Long ownerId;
    
    /** Имя владельца */
    private String ownerUsername;
} 