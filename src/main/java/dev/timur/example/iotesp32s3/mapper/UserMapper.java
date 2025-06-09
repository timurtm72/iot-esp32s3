package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.UserDto;
import dev.timur.example.iotesp32s3.dto.UserReadDto;
import dev.timur.example.iotesp32s3.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Маппер для преобразования между сущностью User и DTO
 * Используется MapStruct с интеграцией Spring
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    
    /**
     * Преобразование сущности User в DTO для чтения (без пароля)
     * @param user сущность пользователя
     * @return DTO пользователя для чтения
     */
    UserReadDto toReadDto(User user);
    
    /**
     * Преобразование сущности User в DTO с паролем
     * @param user сущность пользователя
     * @return DTO пользователя с паролем
     */
    UserDto toDto(User user);
    
    /**
     * Преобразование DTO в сущность User
     * @param dto DTO пользователя
     * @return сущность пользователя
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    User toEntity(UserDto dto);
    
    /**
     * Обновление сущности данными из DTO
     * @param dto DTO с новыми данными
     * @param user существующая сущность для обновления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    void updateEntity(UserDto dto, @MappingTarget User user);
    
    /**
     * Преобразование списка сущностей в список DTO для чтения
     * @param users список сущностей пользователей
     * @return список DTO пользователей для чтения
     */
    List<UserReadDto> toReadDtoList(List<User> users);
    
    /**
     * Преобразование списка сущностей в список DTO с паролями
     * @param users список сущностей пользователей
     * @return список DTO пользователей с паролями
     */
    List<UserDto> toDtoList(List<User> users);
    
    /**
     * Преобразование списка DTO в список сущностей
     * @param dtoList список DTO пользователей
     * @return список сущностей пользователей
     */
    List<User> toEntityList(List<UserDto> dtoList);
} 