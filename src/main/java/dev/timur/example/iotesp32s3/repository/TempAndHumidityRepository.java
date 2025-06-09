package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.TempAndHumidity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с устройствами температуры и влажности
 */
@Repository
public interface TempAndHumidityRepository extends JpaRepository<TempAndHumidity, Long> {
    
    /**
     * Найти все устройства по владельцу
     * @param ownerId идентификатор владельца
     * @return список устройств
     */
    List<TempAndHumidity> findByOwnerId(Long ownerId);
    
    /**
     * Найти устройство по имени и владельцу
     * @param name название устройства
     * @param ownerId идентификатор владельца
     * @return устройство
     */
    Optional<TempAndHumidity> findByNameAndOwnerId(String name, Long ownerId);
    
    /**
     * Найти все активные устройства (не удаленные)
     * @return список активных устройств
     */
    List<TempAndHumidity> findByRemovedAtIsNull();
    
    /**
     * Найти активные устройства по владельцу
     * @param ownerId идентификатор владельца
     * @return список активных устройств владельца
     */
    List<TempAndHumidity> findByOwnerIdAndRemovedAtIsNull(Long ownerId);
    
    /**
     * Найти устройства по локации
     * @param location местоположение
     * @return список устройств в указанной локации
     */
    List<TempAndHumidity> findByLocationContainingIgnoreCase(String location);
} 