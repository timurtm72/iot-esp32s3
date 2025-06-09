package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Маппер для преобразования между сущностью Device и DeviceDto
 * Используется MapStruct с интеграцией Spring
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeviceMapper {
    
    /**
     * Преобразование сущности Device в DTO
     * @param device сущность устройства
     * @return DTO устройства
     */
    @Mapping(target = "ownerId", source = "owner.id")
    DeviceDto toDto(Device device);
    
    /**
     * Преобразование DTO в сущность Device
     * @param dto DTO устройства
     * @return сущность устройства
     */
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "ledStripData", ignore = true)
    @Mapping(target = "tempAndHumidityData", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    Device toEntity(DeviceDto dto);
    
    /**
     * Обновление сущности данными из DTO
     * @param dto DTO с новыми данными
     * @param device существующая сущность для обновления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "ledStripData", ignore = true)
    @Mapping(target = "tempAndHumidityData", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    void updateEntity(DeviceDto dto, @MappingTarget Device device);
    
    /**
     * Преобразование списка сущностей в список DTO
     * @param devices список сущностей устройств
     * @return список DTO устройств
     */
    List<DeviceDto> toDtoList(List<Device> devices);
    
    /**
     * Преобразование списка DTO в список сущностей
     * @param dtoList список DTO устройств
     * @return список сущностей устройств
     */
    List<Device> toEntityList(List<DeviceDto> dtoList);
} 