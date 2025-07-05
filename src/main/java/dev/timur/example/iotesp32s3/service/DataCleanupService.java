package dev.timur.example.iotesp32s3.service;

/**
 * Интерфейс сервиса для удаления устаревших телеметрических данных.
 */
public interface DataCleanupService {

    /**
     * Удаляет записи старше указанного количества дней.
     * @param days количество дней хранения
     */
    void purgeOlderThanDays(long days);
} 