package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDataDto;
import dev.timur.example.iotesp32s3.model.TempAndHumidityData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Маппер для преобразования между сущностью TempAndHumidityData и TempAndHumidityDataDto
 * Используется MapStruct с интеграцией Spring
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TempAndHumidityDataMapper {
    
    /**
     * Преобразование сущности TempAndHumidityData в DTO
     * @param tempAndHumidityData сущность данных температуры и влажности
     * @return DTO данных температуры и влажности
     */
    @Mapping(target = "deviceId", source = "device.id")
    TempAndHumidityDataDto toDto(TempAndHumidityData tempAndHumidityData);
    
    /**
     * Преобразование DTO в сущность TempAndHumidityData
     * @param dto DTO данных температуры и влажности
     * @return сущность данных температуры и влажности
     */
    @Mapping(target = "device", ignore = true)
    TempAndHumidityData toEntity(TempAndHumidityDataDto dto);
    
    /**
     * Обновление сущности данными из DTO
     * @param dto DTO с новыми данными
     * @param tempAndHumidityData существующая сущность для обновления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "device", ignore = true)
    void updateEntity(TempAndHumidityDataDto dto, @MappingTarget TempAndHumidityData tempAndHumidityData);
    
    /**
     * Преобразование списка сущностей в список DTO
     * @param tempAndHumidityDataList список сущностей данных температуры и влажности
     * @return список DTO данных температуры и влажности
     */
    List<TempAndHumidityDataDto> toDtoList(List<TempAndHumidityData> tempAndHumidityDataList);
    
    /**
     * Преобразование списка DTO в список сущностей
     * @param dtoList список DTO данных температуры и влажности
     * @return список сущностей данных температуры и влажности
     */
    List<TempAndHumidityData> toEntityList(List<TempAndHumidityDataDto> dtoList);
} 