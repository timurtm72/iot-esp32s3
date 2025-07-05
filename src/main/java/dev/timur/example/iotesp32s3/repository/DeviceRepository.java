package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями устройств IoT
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    /**
     * Поиск всех активных устройств (не удалённых)
     * @return список активных устройств
     */
    List<Device> findByRemovedAtIsNull();
    
    /**
     * Поиск устройства по названию среди активных устройств
     * @param name название устройства
     * @return опциональное устройство
     */
    Optional<Device> findByNameAndRemovedAtIsNull(String name);
    
    /**
     * Поиск устройств по владельцу среди активных устройств
     * @param ownerId идентификатор владельца
     * @return список устройств владельца
     */
    List<Device> findByOwnerIdAndRemovedAtIsNull(Long ownerId);
    
    /**
     * Поиск устройств по городу среди активных устройств
     * @param city город
     * @return список устройств
     */
    List<Device> findByLocation_CityAndRemovedAtIsNull(String city);
    
    /**
     * Поиск устройств созданных после указанной даты
     * @param createdAfter дата создания
     * @return список устройств
     */
    List<Device> findByCreatedAtAfterAndRemovedAtIsNull(LocalDateTime createdAfter);
} 