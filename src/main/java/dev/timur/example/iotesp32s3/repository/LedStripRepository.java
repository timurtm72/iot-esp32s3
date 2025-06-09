package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.LedStrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с LED лентами
 */
@Repository
public interface LedStripRepository extends JpaRepository<LedStrip, Long> {
    
    /**
     * Найти все устройства по владельцу
     * @param ownerId идентификатор владельца
     * @return список устройств
     */
    List<LedStrip> findByOwnerId(Long ownerId);
    
    /**
     * Найти устройство по имени и владельцу
     * @param name название устройства
     * @param ownerId идентификатор владельца
     * @return устройство
     */
    Optional<LedStrip> findByNameAndOwnerId(String name, Long ownerId);
    
    /**
     * Найти все активные устройства (не удаленные)
     * @return список активных устройств
     */
    List<LedStrip> findByRemovedAtIsNull();
    
    /**
     * Найти активные устройства по владельцу
     * @param ownerId идентификатор владельца
     * @return список активных устройств владельца
     */
    List<LedStrip> findByOwnerIdAndRemovedAtIsNull(Long ownerId);
    
    /**
     * Найти устройства по локации
     * @param location местоположение
     * @return список устройств в указанной локации
     */
    List<LedStrip> findByLocationContainingIgnoreCase(String location);
} 