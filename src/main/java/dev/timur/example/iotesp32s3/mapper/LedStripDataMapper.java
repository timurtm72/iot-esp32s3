package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.LedStripDataDto;
import dev.timur.example.iotesp32s3.model.LedStripData;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Маппер для преобразования между LedStripData и LedStripDataDto
 */
@Mapper(componentModel = "spring")
@Component
public interface LedStripDataMapper {
    
    /**
     * Преобразовать entity в DTO
     * @param entity сущность данных
     * @return DTO данных
     */
    @Mapping(source = "device.id", target = "deviceId")
    LedStripDataDto toDto(LedStripData entity);
    
    /**
     * Преобразовать DTO в entity
     * @param dto DTO данных
     * @return сущность данных
     */
    @Mapping(target = "device", ignore = true)
    LedStripData toEntity(LedStripDataDto dto);
    
    /**
     * Преобразовать список entity в список DTO
     * @param entities список сущностей
     * @return список DTO
     */
    List<LedStripDataDto> toDtoList(List<LedStripData> entities);
    
    /**
     * Обновить entity данными из DTO
     * @param dto DTO с новыми данными
     * @param entity существующая сущность
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "device", ignore = true)
    void updateEntityFromDto(LedStripDataDto dto, @MappingTarget LedStripData entity);
} 