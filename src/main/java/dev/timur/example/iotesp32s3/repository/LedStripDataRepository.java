package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.LedStripData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * Найти все данные по устройству
     * @param deviceId идентификатор устройства
     * @return список данных
     */
    List<LedStripData> findByDeviceId(Long deviceId);
    
    /**
     * Найти данные по устройству за период
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных за период
     */
    List<LedStripData> findByDeviceIdAndTimestampBetweenOrderByTimestampDesc(Long deviceId, 
                                                                            LocalDateTime startTime, 
                                                                            LocalDateTime endTime);
    
    /**
     * Найти записи по устройству, отсортированные по времени (последние сначала)
     * @param deviceId идентификатор устройства
     * @return записи, отсортированные по времени
     */
    List<LedStripData> findByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    /**
     * Найти первую запись по устройству, отсортированную по времени (последняя)
     * @param deviceId идентификатор устройства
     * @return последнее состояние
     */
    Optional<LedStripData> findFirstByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    /**
     * Найти данные по конкретному цвету
     * @param deviceId идентификатор устройства
     * @param redColor красный цвет
     * @param greenColor зеленый цвет
     * @param blueColor синий цвет
     * @return список данных с указанным цветом
     */
    List<LedStripData> findByDeviceIdAndRedColorAndGreenColorAndBlueColorOrderByTimestampDesc(Long deviceId, 
                                                                                             Integer redColor, 
                                                                                             Integer greenColor, 
                                                                                             Integer blueColor);
    
    /**
     * Найти данные по яркости выше указанного значения
     * @param deviceId идентификатор устройства
     * @param brightness минимальная яркость
     * @return записи с яркостью выше указанной
     */
    List<LedStripData> findByDeviceIdAndBrightnessGreaterThan(Long deviceId, Integer brightness);
} 