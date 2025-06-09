package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDataDto;
import dev.timur.example.iotesp32s3.model.TempAndHumidityData;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Маппер для преобразования между TempAndHumidityData и TempAndHumidityDataDto
 */
@Mapper(componentModel = "spring")
@Component
public interface TempAndHumidityDataMapper {
    
    /**
     * Преобразовать entity в DTO
     * @param entity сущность данных
     * @return DTO данных
     */
    @Mapping(source = "device.id", target = "deviceId")
    TempAndHumidityDataDto toDto(TempAndHumidityData entity);
    
    /**
     * Преобразовать DTO в entity
     * @param dto DTO данных
     * @return сущность данных
     */
    @Mapping(target = "device", ignore = true)
    TempAndHumidityData toEntity(TempAndHumidityDataDto dto);
    
    /**
     * Преобразовать список entity в список DTO
     * @param entities список сущностей
     * @return список DTO
     */
    List<TempAndHumidityDataDto> toDtoList(List<TempAndHumidityData> entities);
    
    /**
     * Обновить entity данными из DTO
     * @param dto DTO с новыми данными
     * @param entity существующая сущность
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "device", ignore = true)
    void updateEntityFromDto(TempAndHumidityDataDto dto, @MappingTarget TempAndHumidityData entity);
} 