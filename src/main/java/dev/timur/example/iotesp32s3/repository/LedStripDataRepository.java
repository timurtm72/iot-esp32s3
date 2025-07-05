package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.LedStripData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с данными LED ленты
 */
@Repository
public interface LedStripDataRepository extends JpaRepository<LedStripData, Long> {
    
    /**
     * Поиск данных LED ленты по устройству
     * @param deviceId идентификатор устройства
     * @return список данных LED ленты для устройства
     */
    List<LedStripData> findByDeviceId(Long deviceId);
    
    /**
     * Поиск данных LED ленты по устройству с сортировкой по времени (по убыванию)
     * @param deviceId идентификатор устройства
     * @return список данных LED ленты отсортированный по времени
     */
    List<LedStripData> findByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    /**
     * Поиск последних данных LED ленты для устройства
     * @param deviceId идентификатор устройства
     * @return последние данные LED ленты
     */
    Optional<LedStripData> findFirstByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    /**
     * Поиск данных LED ленты по устройству в заданном временном диапазоне
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных LED ленты в указанном диапазоне
     */
    List<LedStripData> findByDeviceIdAndTimestampBetween(Long deviceId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Поиск данных LED ленты по яркости больше указанного значения
     * @param brightness минимальная яркость
     * @return список данных LED ленты
     */
    List<LedStripData> findByBrightnessGreaterThan(Integer brightness);

    /**
     * Удалить данные старше указанного времени
     * @param threshold временной порог
     */
    void deleteByTimestampBefore(LocalDateTime threshold);
} 