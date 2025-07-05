package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.repository.LedStripDataRepository;
import dev.timur.example.iotesp32s3.repository.TempAndHumidityDataRepository;
import dev.timur.example.iotesp32s3.service.DataCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Реализация сервиса очистки устаревших телеметрических данных.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DataCleanupServiceImpl implements DataCleanupService {

    private final LedStripDataRepository ledStripDataRepository;
    private final TempAndHumidityDataRepository tempAndHumidityDataRepository;

    @Override
    @Transactional
    public void purgeOlderThanDays(long days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        log.info("Удаляем данные старше {} дней (порог: {})", days, threshold);
        ledStripDataRepository.deleteByTimestampBefore(threshold);
        tempAndHumidityDataRepository.deleteByTimestampBefore(threshold);
    }
} 