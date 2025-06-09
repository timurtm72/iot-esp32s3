package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.LedStripDataDto;
import dev.timur.example.iotesp32s3.mapper.LedStripDataMapper;
import dev.timur.example.iotesp32s3.model.LedStrip;
import dev.timur.example.iotesp32s3.model.LedStripData;
import dev.timur.example.iotesp32s3.repository.LedStripDataRepository;
import dev.timur.example.iotesp32s3.repository.LedStripRepository;
import dev.timur.example.iotesp32s3.service.LedStripDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса для работы с данными LED ленты
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LedStripDataServiceImpl implements LedStripDataService {

    private final LedStripDataRepository dataRepository;
    private final LedStripRepository deviceRepository;
    private final LedStripDataMapper mapper;

    /**
     * Создать новую запись данных
     * @param dataDto данные LED ленты
     * @return созданная запись
     */
    @Override
    @Transactional
    public LedStripDataDto createData(LedStripDataDto dataDto) {
        log.info("Создание новой записи данных для LED устройства ID: {}", dataDto.getDeviceId());
        
        // Получаем устройство по ID
        LedStrip device = deviceRepository.findById(dataDto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Устройство не найдено с ID: " + dataDto.getDeviceId()));
        
        // Преобразуем DTO в entity
        LedStripData data = mapper.toEntity(dataDto);
        data.setDevice(device);
        
        // Устанавливаем текущее время если не указано
        if (data.getTimestamp() == null) {
            data.setTimestamp(LocalDateTime.now());
        }
        
        // Сохраняем данные
        LedStripData savedData = dataRepository.save(data);
        
        log.info("Запись данных LED создана с ID: {}", savedData.getId());
        return mapper.toDto(savedData);
    }

    /**
     * Получить запись по ID
     * @param id идентификатор записи
     * @return запись данных
     */
    @Override
    public LedStripDataDto getDataById(Long id) {
        log.debug("Получение записи данных LED по ID: {}", id);
        
        LedStripData data = dataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись данных не найдена с ID: " + id));
        
        return mapper.toDto(data);
    }

    /**
     * Получить все данные устройства
     * @param deviceId идентификатор устройства
     * @return список данных
     */
    @Override
    public List<LedStripDataDto> getDataByDeviceId(Long deviceId) {
        log.debug("Получение всех данных LED устройства с ID: {}", deviceId);
        
        List<LedStripData> dataList = dataRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
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
    public List<LedStripDataDto> getDataByPeriod(Long deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Получение данных LED устройства ID: {} за период с {} по {}", deviceId, startTime, endTime);
        
        List<LedStripData> dataList = dataRepository.findByDeviceIdAndTimestampBetweenOrderByTimestampDesc(deviceId, startTime, endTime);
        return mapper.toDtoList(dataList);
    }

    /**
     * Получить последние данные устройства
     * @param deviceId идентификатор устройства
     * @param limit количество записей
     * @return последние записи
     */
    @Override
    public List<LedStripDataDto> getLatestData(Long deviceId, int limit) {
        log.debug("Получение последних {} записей для LED устройства ID: {}", limit, deviceId);
        
        List<LedStripData> dataList = dataRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
        
        // Ограничиваем количество результатов
        List<LedStripData> limitedData = dataList.stream()
                .limit(limit)
                .toList();
        
        return mapper.toDtoList(limitedData);
    }

    /**
     * Получить текущее состояние LED ленты
     * @param deviceId идентификатор устройства
     * @return текущее состояние
     */
    @Override
    public LedStripDataDto getCurrentState(Long deviceId) {
        log.debug("Получение текущего состояния LED устройства с ID: {}", deviceId);
        
        LedStripData currentState = dataRepository.findFirstByDeviceIdOrderByTimestampDesc(deviceId)
                .orElseThrow(() -> new RuntimeException("Состояние LED устройства не найдено для ID: " + deviceId));
        
        return mapper.toDto(currentState);
    }

    /**
     * Обновить запись данных
     * @param id идентификатор записи
     * @param dataDto новые данные
     * @return обновленная запись
     */
    @Override
    @Transactional
    public LedStripDataDto updateData(Long id, LedStripDataDto dataDto) {
        log.info("Обновление записи данных LED с ID: {}", id);
        
        LedStripData existingData = dataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись данных не найдена с ID: " + id));
        
        // Обновляем поля записи
        mapper.updateEntityFromDto(dataDto, existingData);
        
        // Сохраняем изменения
        LedStripData updatedData = dataRepository.save(existingData);
        
        log.info("Запись данных LED обновлена с ID: {}", updatedData.getId());
        return mapper.toDto(updatedData);
    }

    /**
     * Удалить запись данных
     * @param id идентификатор записи
     */
    @Override
    @Transactional
    public void deleteData(Long id) {
        log.info("Удаление записи данных LED с ID: {}", id);
        
        if (!dataRepository.existsById(id)) {
            throw new RuntimeException("Запись данных не найдена с ID: " + id);
        }
        
        dataRepository.deleteById(id);
        log.info("Запись данных LED удалена с ID: {}", id);
    }

    /**
     * Получить данные по цвету
     * @param deviceId идентификатор устройства
     * @param redColor красный цвет
     * @param greenColor зеленый цвет
     * @param blueColor синий цвет
     * @return записи с указанным цветом
     */
    @Override
    public List<LedStripDataDto> getDataByColor(Long deviceId, Integer redColor, Integer greenColor, Integer blueColor) {
        log.debug("Получение данных LED устройства ID: {} с цветом RGB({}, {}, {})", deviceId, redColor, greenColor, blueColor);
        
        List<LedStripData> dataList = dataRepository.findByDeviceIdAndRedColorAndGreenColorAndBlueColorOrderByTimestampDesc(
                deviceId, redColor, greenColor, blueColor);
        return mapper.toDtoList(dataList);
    }
} 