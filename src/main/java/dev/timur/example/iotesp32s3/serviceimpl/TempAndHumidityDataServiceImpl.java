package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDataDto;
import dev.timur.example.iotesp32s3.mapper.TempAndHumidityDataMapper;
import dev.timur.example.iotesp32s3.model.TempAndHumidity;
import dev.timur.example.iotesp32s3.model.TempAndHumidityData;
import dev.timur.example.iotesp32s3.repository.TempAndHumidityDataRepository;
import dev.timur.example.iotesp32s3.repository.TempAndHumidityRepository;
import dev.timur.example.iotesp32s3.service.TempAndHumidityDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса для работы с данными датчиков температуры и влажности
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TempAndHumidityDataServiceImpl implements TempAndHumidityDataService {

    private final TempAndHumidityDataRepository dataRepository;
    private final TempAndHumidityRepository deviceRepository;
    private final TempAndHumidityDataMapper mapper;

    /**
     * Создать новую запись данных
     * @param dataDto данные с датчиков
     * @return созданная запись
     */
    @Override
    @Transactional
    public TempAndHumidityDataDto createData(TempAndHumidityDataDto dataDto) {
        log.info("Создание новой записи данных для устройства ID: {}", dataDto.getDeviceId());
        
        // Получаем устройство по ID
        TempAndHumidity device = deviceRepository.findById(dataDto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Устройство не найдено с ID: " + dataDto.getDeviceId()));
        
        // Преобразуем DTO в entity
        TempAndHumidityData data = mapper.toEntity(dataDto);
        data.setDevice(device);
        
        // Устанавливаем текущее время если не указано
        if (data.getTimestamp() == null) {
            data.setTimestamp(LocalDateTime.now());
        }
        
        // Сохраняем данные
        TempAndHumidityData savedData = dataRepository.save(data);
        
        log.info("Запись данных создана с ID: {}", savedData.getId());
        return mapper.toDto(savedData);
    }

    /**
     * Получить запись по ID
     * @param id идентификатор записи
     * @return запись данных
     */
    @Override
    public TempAndHumidityDataDto getDataById(Long id) {
        log.debug("Получение записи данных по ID: {}", id);
        
        TempAndHumidityData data = dataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись данных не найдена с ID: " + id));
        
        return mapper.toDto(data);
    }

    /**
     * Получить все данные устройства
     * @param deviceId идентификатор устройства
     * @return список данных
     */
    @Override
    public List<TempAndHumidityDataDto> getDataByDeviceId(Long deviceId) {
        log.debug("Получение всех данных устройства с ID: {}", deviceId);
        
        List<TempAndHumidityData> dataList = dataRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
        return mapper.toDtoList(dataList);
    }

    /**
     * Получить данные за период
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных за период
     */
    @Override
    public List<TempAndHumidityDataDto> getDataByPeriod(Long deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Получение данных устройства ID: {} за период с {} по {}", deviceId, startTime, endTime);
        
        List<TempAndHumidityData> dataList = dataRepository.findByDeviceIdAndTimestampBetweenOrderByTimestampDesc(deviceId, startTime, endTime);
        return mapper.toDtoList(dataList);
    }

    /**
     * Получить последние данные устройства
     * @param deviceId идентификатор устройства
     * @param limit количество записей
     * @return последние записи
     */
    @Override
    public List<TempAndHumidityDataDto> getLatestData(Long deviceId, int limit) {
        log.debug("Получение последних {} записей для устройства ID: {}", limit, deviceId);
        
        Pageable pageable = PageRequest.of(0, limit);
        List<TempAndHumidityData> dataList = dataRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
        
        // Ограничиваем количество результатов
        List<TempAndHumidityData> limitedData = dataList.stream()
                .limit(limit)
                .toList();
        
        return mapper.toDtoList(limitedData);
    }

    /**
     * Обновить запись данных
     * @param id идентификатор записи
     * @param dataDto новые данные
     * @return обновленная запись
     */
    @Override
    @Transactional
    public TempAndHumidityDataDto updateData(Long id, TempAndHumidityDataDto dataDto) {
        log.info("Обновление записи данных с ID: {}", id);
        
        TempAndHumidityData existingData = dataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись данных не найдена с ID: " + id));
        
        // Обновляем поля записи
        mapper.updateEntityFromDto(dataDto, existingData);
        
        // Сохраняем изменения
        TempAndHumidityData updatedData = dataRepository.save(existingData);
        
        log.info("Запись данных обновлена с ID: {}", updatedData.getId());
        return mapper.toDto(updatedData);
    }

    /**
     * Удалить запись данных
     * @param id идентификатор записи
     */
    @Override
    @Transactional
    public void deleteData(Long id) {
        log.info("Удаление записи данных с ID: {}", id);
        
        if (!dataRepository.existsById(id)) {
            throw new RuntimeException("Запись данных не найдена с ID: " + id);
        }
        
        dataRepository.deleteById(id);
        log.info("Запись данных удалена с ID: {}", id);
    }

    /**
     * Получить данные с высокой температурой
     * @param deviceId идентификатор устройства
     * @param minTemperature минимальная температура
     * @return записи с температурой выше указанной
     */
    @Override
    public List<TempAndHumidityDataDto> getHighTemperatureData(Long deviceId, Float minTemperature) {
        log.debug("Получение данных с температурой выше {} для устройства ID: {}", minTemperature, deviceId);
        
        List<TempAndHumidityData> dataList = dataRepository.findByDeviceIdAndTemperatureGreaterThan(deviceId, minTemperature);
        return mapper.toDtoList(dataList);
    }
} 