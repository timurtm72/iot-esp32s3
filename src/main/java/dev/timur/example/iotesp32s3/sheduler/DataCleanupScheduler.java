package dev.timur.example.iotesp32s3.sheduler;

import dev.timur.example.iotesp32s3.service.DataCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Планировщик, который регулярно запускает очистку устаревших данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataCleanupScheduler {

    private final DataCleanupService cleanupService;

    /** Количество дней для хранения записей телеметрии. */
    @Value("${data.retention-days:30}")
    private long retentionDays;

    /**
     * Ежедневный запуск в 03:00 (можно переопределить через data.cleanup.cron).
     */
    @Scheduled(cron = "${data.cleanup.cron:0 0 3 * * *}")
    public void scheduleCleanup() {
        log.info("Запуск плановой очистки (retention={} дней)", retentionDays);
        cleanupService.purgeOlderThanDays(retentionDays);
    }
} 