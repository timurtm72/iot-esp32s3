package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.LedStripDto;
import dev.timur.example.iotesp32s3.model.LedStrip;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Маппер для преобразования между LedStrip и LedStripDto
 */
@Mapper(componentModel = "spring", uses = {LedStripDataMapper.class})
@Component
public interface LedStripMapper {
    
    /**
     * Преобразовать entity в DTO
     * @param entity сущность устройства
     * @return DTO устройства
     */
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.username", target = "ownerUsername")
    LedStripDto toDto(LedStrip entity);
    
    /**
     * Преобразовать DTO в entity
     * @param dto DTO устройства
     * @return сущность устройства
     */
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "dataValues", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    LedStrip toEntity(LedStripDto dto);
    
    /**
     * Преобразовать список entity в список DTO
     * @param entities список сущностей
     * @return список DTO
     */
    List<LedStripDto> toDtoList(List<LedStrip> entities);
    
    /**
     * Обновить entity данными из DTO
     * @param dto DTO с новыми данными
     * @param entity существующая сущность
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "dataValues", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    void updateEntityFromDto(LedStripDto dto, @MappingTarget LedStrip entity);
} 