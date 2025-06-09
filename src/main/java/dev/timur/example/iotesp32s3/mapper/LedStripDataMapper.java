package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.LedStripDataDto;
import dev.timur.example.iotesp32s3.model.LedStripData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Маппер для преобразования между сущностью LedStripData и LedStripDataDto
 * Используется MapStruct с интеграцией Spring
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LedStripDataMapper {
    
    /**
     * Преобразование сущности LedStripData в DTO
     * @param ledStripData сущность данных LED ленты
     * @return DTO данных LED ленты
     */
    @Mapping(target = "deviceId", source = "device.id")
    LedStripDataDto toDto(LedStripData ledStripData);
    
    /**
     * Преобразование DTO в сущность LedStripData
     * @param dto DTO данных LED ленты
     * @return сущность данных LED ленты
     */
    @Mapping(target = "device", ignore = true)
    LedStripData toEntity(LedStripDataDto dto);
    
    /**
     * Обновление сущности данными из DTO
     * @param dto DTO с новыми данными
     * @param ledStripData существующая сущность для обновления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "device", ignore = true)
    void updateEntity(LedStripDataDto dto, @MappingTarget LedStripData ledStripData);
    
    /**
     * Преобразование списка сущностей в список DTO
     * @param ledStripDataList список сущностей данных LED ленты
     * @return список DTO данных LED ленты
     */
    List<LedStripDataDto> toDtoList(List<LedStripData> ledStripDataList);
    
    /**
     * Преобразование списка DTO в список сущностей
     * @param dtoList список DTO данных LED ленты
     * @return список сущностей данных LED ленты
     */
    List<LedStripData> toEntityList(List<LedStripDataDto> dtoList);
} 