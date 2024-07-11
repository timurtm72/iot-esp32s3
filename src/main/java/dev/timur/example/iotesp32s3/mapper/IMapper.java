package dev.timur.example.iotesp32s3.mapper;

public interface IMapper<Entity,Dto> {
    Dto toDto(Entity entity);
    Entity toEntity(Dto dto);
}