package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.LedStripDataDto;
import dev.timur.example.iotesp32s3.mapper.LedStripDataMapper;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.model.LedStripData;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.repository.LedStripDataRepository;
import dev.timur.example.iotesp32s3.service.LedStripDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с данными LED ленты
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LedStripDataServiceImpl implements LedStripDataService {
    
    private final LedStripDataRepository ledStripDataRepository;
    private final DeviceRepository deviceRepository;
    private final LedStripDataMapper ledStripDataMapper;
    
    /**
     * Создание новой записи данных LED ленты
     * @param ledStripDataDto данные LED ленты
     * @return созданная запись
     */
    @Override
    @Transactional
    public LedStripDataDto createLedStripData(LedStripDataDto ledStripDataDto) {
        log.info("Создание новой записи данных LED ленты для устройства с ID: {}", ledStripDataDto.getDeviceId());
        
        LedStripData ledStripData = ledStripDataMapper.toEntity(ledStripDataDto);
        
        // Установка устройства
        if (ledStripDataDto.getDeviceId() != null) {
            Optional<Device> device = deviceRepository.findById(ledStripDataDto.getDeviceId());
            if (device.isPresent()) {
                ledStripData.setDevice(device.get());
            } else {
                log.warn("Устройство с ID {} не найдено", ledStripDataDto.getDeviceId());
                throw new IllegalArgumentException("Устройство не найдено");
            }
        }
        
        if (ledStripData.getTimestamp() == null) {
            ledStripData.setTimestamp(LocalDateTime.now());
        }
        
        LedStripData savedData = ledStripDataRepository.save(ledStripData);
        log.info("Данные LED ленты успешно сохранены с ID: {}", savedData.getId());
        
        return ledStripDataMapper.toDto(savedData);
    }
    
    /**
     * Получение записи данных LED ленты по идентификатору
     * @param id идентификатор записи
     * @return запись данных или пустой Optional
     */
    @Override
    public Optional<LedStripDataDto> getLedStripDataById(Long id) {
        log.debug("Поиск данных LED ленты по ID: {}", id);
        
        return ledStripDataRepository.findById(id)
                .map(ledStripDataMapper::toDto);
    }
    
    /**
     * Обновление записи данных LED ленты
     * @param id идентификатор записи
     * @param ledStripDataDto новые данные
     * @return обновленная запись
     */
    @Override
    @Transactional
    public Optional<LedStripDataDto> updateLedStripData(Long id, LedStripDataDto ledStripDataDto) {
        log.info("Обновление данных LED ленты с ID: {}", id);
        
        return ledStripDataRepository.findById(id)
                .map(existingData -> {
                    ledStripDataMapper.updateEntity(ledStripDataDto, existingData);
                    
                    // Обновление устройства если изменилось
                    if (ledStripDataDto.getDeviceId() != null) {
                        Optional<Device> newDevice = deviceRepository.findById(ledStripDataDto.getDeviceId());
                        if (newDevice.isPresent()) {
                            existingData.setDevice(newDevice.get());
                        }
                    }
                    
                    LedStripData updatedData = ledStripDataRepository.save(existingData);
                    log.info("Данные LED ленты с ID {} успешно обновлены", id);
                    
                    return ledStripDataMapper.toDto(updatedData);
                });
    }
    
    /**
     * Удаление записи данных LED ленты
     * @param id идентификатор записи
     * @return true если запись удалена, false если не найдена
     */
    @Override
    @Transactional
    public boolean deleteLedStripData(Long id) {
        log.info("Удаление данных LED ленты с ID: {}", id);
        
        if (ledStripDataRepository.existsById(id)) {
            ledStripDataRepository.deleteById(id);
            log.info("Данные LED ленты с ID {} успешно удалены", id);
            return true;
        }
        return false;
    }
    
    /**
     * Получение всех данных LED ленты
     * @return список всех записей
     */
    @Override
    public List<LedStripDataDto> getAllLedStripData() {
        log.debug("Получение всех данных LED ленты");
        
        List<LedStripData> dataList = ledStripDataRepository.findAll();
        return ledStripDataMapper.toDtoList(dataList);
    }
    
    /**
     * Получение данных LED ленты по устройству
     * @param deviceId идентификатор устройства
     * @return список данных для устройства
     */
    @Override
    @Cacheable(value = "ledStripDataByDevice", key = "#deviceId")
    public List<LedStripDataDto> getLedStripDataByDevice(Long deviceId) {
        log.debug("Получение данных LED ленты для устройства с ID: {}", deviceId);
        
        List<LedStripData> dataList = ledStripDataRepository.findByDeviceId(deviceId);
        return ledStripDataMapper.toDtoList(dataList);
    }
    
    /**
     * Получение данных LED ленты по устройству отсортированных по времени
     * @param deviceId идентификатор устройства
     * @return список данных отсортированный по времени
     */
    @Override
    public List<LedStripDataDto> getLedStripDataByDeviceOrderByTime(Long deviceId) {
        log.debug("Получение данных LED ленты для устройства с ID {} отсортированных по времени", deviceId);
        
        List<LedStripData> dataList = ledStripDataRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
        return ledStripDataMapper.toDtoList(dataList);
    }
    
    /**
     * Получение последних данных LED ленты для устройства
     * @param deviceId идентификатор устройства
     * @return последние данные или пустой Optional
     */
    @Override
    @Cacheable(value = "latestLedStripData", key = "#deviceId")
    public Optional<LedStripDataDto> getLatestLedStripDataByDevice(Long deviceId) {
        log.debug("Получение последних данных LED ленты для устройства с ID: {}", deviceId);
        
        return ledStripDataRepository.findFirstByDeviceIdOrderByTimestampDesc(deviceId)
                .map(ledStripDataMapper::toDto);
    }
    
    /**
     * Получение данных LED ленты в заданном временном диапазоне
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных в указанном диапазоне
     */
    @Override
    public List<LedStripDataDto> getLedStripDataByDeviceAndTimeRange(Long deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Получение данных LED ленты для устройства с ID {} в диапазоне: {} - {}", deviceId, startTime, endTime);
        
        List<LedStripData> dataList = ledStripDataRepository.findByDeviceIdAndTimestampBetween(deviceId, startTime, endTime);
        return ledStripDataMapper.toDtoList(dataList);
    }
    
    /**
     * Получение данных LED ленты с яркостью больше указанного значения
     * @param brightness минимальная яркость
     * @return список данных
     */
    @Override
    public List<LedStripDataDto> getLedStripDataByBrightnessGreaterThan(Integer brightness) {
        log.debug("Получение данных LED ленты с яркостью больше: {}", brightness);
        
        List<LedStripData> dataList = ledStripDataRepository.findByBrightnessGreaterThan(brightness);
        return ledStripDataMapper.toDtoList(dataList);
    }
} 