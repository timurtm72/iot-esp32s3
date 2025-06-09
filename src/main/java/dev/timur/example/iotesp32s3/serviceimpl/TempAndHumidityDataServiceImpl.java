package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDataDto;
import dev.timur.example.iotesp32s3.mapper.TempAndHumidityDataMapper;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.model.TempAndHumidityData;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.repository.TempAndHumidityDataRepository;
import dev.timur.example.iotesp32s3.service.TempAndHumidityDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с данными температуры и влажности
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TempAndHumidityDataServiceImpl implements TempAndHumidityDataService {
    
    private final TempAndHumidityDataRepository tempAndHumidityDataRepository;
    private final DeviceRepository deviceRepository;
    private final TempAndHumidityDataMapper tempAndHumidityDataMapper;
    
    /**
     * Создание новой записи данных температуры и влажности
     * @param tempAndHumidityDataDto данные температуры и влажности
     * @return созданная запись
     */
    @Override
    @Transactional
    public TempAndHumidityDataDto createTempAndHumidityData(TempAndHumidityDataDto tempAndHumidityDataDto) {
        log.info("Создание новой записи данных температуры и влажности для устройства с ID: {}", tempAndHumidityDataDto.getDeviceId());
        
        TempAndHumidityData tempAndHumidityData = tempAndHumidityDataMapper.toEntity(tempAndHumidityDataDto);
        
        // Установка устройства
        if (tempAndHumidityDataDto.getDeviceId() != null) {
            Optional<Device> device = deviceRepository.findById(tempAndHumidityDataDto.getDeviceId());
            if (device.isPresent()) {
                tempAndHumidityData.setDevice(device.get());
            } else {
                log.warn("Устройство с ID {} не найдено", tempAndHumidityDataDto.getDeviceId());
                throw new IllegalArgumentException("Устройство не найдено");
            }
        }
        
        if (tempAndHumidityData.getTimestamp() == null) {
            tempAndHumidityData.setTimestamp(LocalDateTime.now());
        }
        
        TempAndHumidityData savedData = tempAndHumidityDataRepository.save(tempAndHumidityData);
        log.info("Данные температуры и влажности успешно сохранены с ID: {}", savedData.getId());
        
        return tempAndHumidityDataMapper.toDto(savedData);
    }
    
    /**
     * Получение записи данных по идентификатору
     * @param id идентификатор записи
     * @return запись данных или пустой Optional
     */
    @Override
    public Optional<TempAndHumidityDataDto> getTempAndHumidityDataById(Long id) {
        log.debug("Поиск данных температуры и влажности по ID: {}", id);
        
        return tempAndHumidityDataRepository.findById(id)
                .map(tempAndHumidityDataMapper::toDto);
    }
    
    /**
     * Обновление записи данных температуры и влажности
     * @param id идентификатор записи
     * @param tempAndHumidityDataDto новые данные
     * @return обновленная запись
     */
    @Override
    @Transactional
    public Optional<TempAndHumidityDataDto> updateTempAndHumidityData(Long id, TempAndHumidityDataDto tempAndHumidityDataDto) {
        log.info("Обновление данных температуры и влажности с ID: {}", id);
        
        return tempAndHumidityDataRepository.findById(id)
                .map(existingData -> {
                    tempAndHumidityDataMapper.updateEntity(tempAndHumidityDataDto, existingData);
                    
                    // Обновление устройства если изменилось
                    if (tempAndHumidityDataDto.getDeviceId() != null) {
                        Optional<Device> newDevice = deviceRepository.findById(tempAndHumidityDataDto.getDeviceId());
                        if (newDevice.isPresent()) {
                            existingData.setDevice(newDevice.get());
                        }
                    }
                    
                    TempAndHumidityData updatedData = tempAndHumidityDataRepository.save(existingData);
                    log.info("Данные температуры и влажности с ID {} успешно обновлены", id);
                    
                    return tempAndHumidityDataMapper.toDto(updatedData);
                });
    }
    
    /**
     * Удаление записи данных
     * @param id идентификатор записи
     * @return true если запись удалена, false если не найдена
     */
    @Override
    @Transactional
    public boolean deleteTempAndHumidityData(Long id) {
        log.info("Удаление данных температуры и влажности с ID: {}", id);
        
        if (tempAndHumidityDataRepository.existsById(id)) {
            tempAndHumidityDataRepository.deleteById(id);
            log.info("Данные температуры и влажности с ID {} успешно удалены", id);
            return true;
        }
        return false;
    }
    
    /**
     * Получение всех данных температуры и влажности
     * @return список всех записей
     */
    @Override
    public List<TempAndHumidityDataDto> getAllTempAndHumidityData() {
        log.debug("Получение всех данных температуры и влажности");
        
        List<TempAndHumidityData> dataList = tempAndHumidityDataRepository.findAll();
        return tempAndHumidityDataMapper.toDtoList(dataList);
    }
    
    /**
     * Получение данных по устройству
     * @param deviceId идентификатор устройства
     * @return список данных для устройства
     */
    @Override
    @Cacheable(value = "tempHumidityDataByDevice", key = "#deviceId")
    public List<TempAndHumidityDataDto> getTempAndHumidityDataByDevice(Long deviceId) {
        log.debug("Получение данных температуры и влажности для устройства с ID: {}", deviceId);
        
        List<TempAndHumidityData> dataList = tempAndHumidityDataRepository.findByDeviceId(deviceId);
        return tempAndHumidityDataMapper.toDtoList(dataList);
    }
    
    /**
     * Получение данных по устройству отсортированных по времени
     * @param deviceId идентификатор устройства
     * @return список данных отсортированный по времени
     */
    @Override
    public List<TempAndHumidityDataDto> getTempAndHumidityDataByDeviceOrderByTime(Long deviceId) {
        log.debug("Получение данных температуры и влажности для устройства с ID {} отсортированных по времени", deviceId);
        
        List<TempAndHumidityData> dataList = tempAndHumidityDataRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
        return tempAndHumidityDataMapper.toDtoList(dataList);
    }
    
    /**
     * Получение последних данных для устройства
     * @param deviceId идентификатор устройства
     * @return последние данные или пустой Optional
     */
    @Override
    @Cacheable(value = "latestTempHumidityData", key = "#deviceId")
    public Optional<TempAndHumidityDataDto> getLatestTempAndHumidityDataByDevice(Long deviceId) {
        log.debug("Получение последних данных температуры и влажности для устройства с ID: {}", deviceId);
        
        return tempAndHumidityDataRepository.findFirstByDeviceIdOrderByTimestampDesc(deviceId)
                .map(tempAndHumidityDataMapper::toDto);
    }
    
    /**
     * Получение данных в заданном временном диапазоне
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных в указанном диапазоне
     */
    @Override
    public List<TempAndHumidityDataDto> getTempAndHumidityDataByDeviceAndTimeRange(Long deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Получение данных температуры и влажности для устройства с ID {} в диапазоне: {} - {}", deviceId, startTime, endTime);
        
        List<TempAndHumidityData> dataList = tempAndHumidityDataRepository.findByDeviceIdAndTimestampBetween(deviceId, startTime, endTime);
        return tempAndHumidityDataMapper.toDtoList(dataList);
    }
    
    /**
     * Получение данных с температурой больше указанного значения
     * @param temperature минимальная температура
     * @return список данных
     */
    @Override
    public List<TempAndHumidityDataDto> getTempAndHumidityDataByTemperatureGreaterThan(Float temperature) {
        log.debug("Получение данных температуры и влажности с температурой больше: {}", temperature);
        
        List<TempAndHumidityData> dataList = tempAndHumidityDataRepository.findByTemperatureGreaterThan(temperature);
        return tempAndHumidityDataMapper.toDtoList(dataList);
    }
    
    /**
     * Получение данных с влажностью больше указанного значения
     * @param humidity минимальная влажность
     * @return список данных
     */
    @Override
    public List<TempAndHumidityDataDto> getTempAndHumidityDataByHumidityGreaterThan(Float humidity) {
        log.debug("Получение данных температуры и влажности с влажностью больше: {}", humidity);
        
        List<TempAndHumidityData> dataList = tempAndHumidityDataRepository.findByHumidityGreaterThan(humidity);
        return tempAndHumidityDataMapper.toDtoList(dataList);
    }
} 