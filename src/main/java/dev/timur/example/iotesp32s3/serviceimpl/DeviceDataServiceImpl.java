package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.DeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.mapper.DeviceDataMapper;
import dev.timur.example.iotesp32s3.model.DeviceData;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.repository.DeviceDataRepository;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.service.DeviceDataService;
import dev.timur.example.iotesp32s3.specification.DeviceDataSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

/**
 * Реализация сервиса для управления данными телеметрии IoT устройств ESP32S3.
 * Предоставляет полный набор операций для работы с временными рядами данных,
 * включая аналитику, агрегацию, обнаружение аномалий и статистику.
 * 
 * Особенности:
 * - Кеширование для оптимизации частых запросов
 * - Поддержка больших объемов временных данных
 * - Современные методы аналитики и агрегации
 * - Интеграция с устройствами через DeviceRepository
 * - Транзакционная безопасность для операций изменения данных
 */
@Service
public class DeviceDataServiceImpl implements DeviceDataService {
    
    /** Репозиторий для работы с данными телеметрии */
    private final DeviceDataRepository deviceDataRepository;
    
    /** Маппер для преобразования между DTO и Entity */
    private final DeviceDataMapper deviceDataMapper;
    
    /** Репозиторий для работы с устройствами */
    private final DeviceRepository deviceRepository;

    /**
     * Конструктор сервиса с внедрением зависимостей.
     * 
     * @param deviceDataRepository репозиторий для работы с данными телеметрии
     * @param deviceDataMapper маппер для преобразования объектов
     * @param deviceRepository репозиторий для работы с устройствами
     */
    @Autowired
    public DeviceDataServiceImpl(DeviceDataRepository deviceDataRepository, DeviceDataMapper deviceDataMapper, 
                                DeviceRepository deviceRepository) {
        this.deviceDataRepository = deviceDataRepository;
        this.deviceDataMapper = deviceDataMapper;
        this.deviceRepository = deviceRepository;
    }

    // Основные CRUD операции

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется для оптимизации повторных запросов.
     */
    @Transactional
    @Override
    @Cacheable(value = "deviceData", key = "#id")
    public DeviceDataDto getById(Long id) {
        Optional<DeviceData> deviceData = deviceDataRepository.findById(id);
        return deviceData.map(deviceDataMapper::toDto).orElse(null);
    }

    /**
     * {@inheritDoc}
     * 
     * ВНИМАНИЕ: Может вернуть большое количество записей!
     * Результат кешируется для оптимизации.
     */
    @Transactional
    @Override
    @Cacheable(value = "allDeviceData")
    public List<DeviceDataDto> getAll() {
        List<DeviceData> deviceDataList = deviceDataRepository.findAll();
        return deviceDataList.stream()
                .map(deviceDataMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Выполняется в транзакции и очищает связанные кеши.
     * Проверяет существование устройства перед созданием записи.
     */
    @Transactional
    @Override
    @CacheEvict(value = {"deviceData", "allDeviceData", "deviceDataByDevice", "latestDeviceData"}, allEntries = true)
    public Status create(DeviceDataDto deviceDataDto, Long deviceId) {
        if (deviceDataDto == null) {
            return Status.IS_EMPTY;
        }
        
        Optional<Device> device = deviceRepository.findByIdAndRemovedAtIsNull(deviceId);
        if (device.isEmpty()) {
            return Status.IS_NOT_FOUND;
        }
        
        DeviceData deviceData = deviceDataMapper.toEntity(deviceDataDto);
        deviceData.setDevice(device.get());
        deviceDataRepository.save(deviceData);
        return Status.IS_OK;
    }

    /**
     * {@inheritDoc}
     * 
     * Выполняется в транзакции и очищает связанные кеши.
     * Обновляет только переданные поля (partial update).
     */
    @Transactional
    @Override
    @CacheEvict(value = {"deviceData", "allDeviceData", "deviceDataByDevice", "latestDeviceData"}, allEntries = true)
    public Status update(DeviceDataDto deviceDataDto, Long id) {
        if (deviceDataDto == null) {
            return Status.IS_EMPTY;
        }
        
        Optional<DeviceData> existingDataOpt = deviceDataRepository.findById(id);
        if (existingDataOpt.isEmpty()) {
            return Status.IS_NOT_FOUND;
        }
        
        DeviceData existingData = existingDataOpt.get();
        
        // Обновляем только непустые поля
        if (deviceDataDto.getHumidity() != null) {
            existingData.setHumidity(deviceDataDto.getHumidity());
        }
        if (deviceDataDto.getTemperature() != null) {
            existingData.setTemperature(deviceDataDto.getTemperature());
        }
        if (deviceDataDto.getRedColor() != null) {
            existingData.setRedColor(deviceDataDto.getRedColor());
        }
        if (deviceDataDto.getGreenColor() != null) {
            existingData.setGreenColor(deviceDataDto.getGreenColor());
        }
        if (deviceDataDto.getBlueColor() != null) {
            existingData.setBlueColor(deviceDataDto.getBlueColor());
        }
        if (deviceDataDto.getBrightness() != null) {
            existingData.setBrightness(deviceDataDto.getBrightness());
        }
        
        deviceDataRepository.save(existingData);
        return Status.IS_OK;
    }

    /**
     * {@inheritDoc}
     * 
     * Выполняется в транзакции и очищает связанные кеши.
     */
    @Transactional
    @Override
    @CacheEvict(value = {"deviceData", "allDeviceData", "deviceDataByDevice", "latestDeviceData"}, allEntries = true)
    public Status delete(Long id) {
        Optional<DeviceData> deviceData = deviceDataRepository.findById(id);
        if (deviceData.isEmpty()) {
            return Status.IS_NULL;
        }
        deviceDataRepository.deleteById(id);
        return Status.IS_OK;
    }

    // Методы поиска и фильтрации

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется по ID устройства для оптимизации.
     * Данные отсортированы по времени (новые первые).
     */
    @Override
    @Cacheable(value = "deviceDataByDevice", key = "#deviceId")
    public List<DeviceDataDto> findByDeviceId(Long deviceId) {
        List<DeviceData> deviceDataList = deviceDataRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
        return deviceDataList.stream()
                .map(deviceDataMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Использует оптимизированный запрос с сортировкой по времени.
     */
    @Override
    public List<DeviceDataDto> findByTimestampBetween(LocalDateTime start, LocalDateTime end) {
        List<DeviceData> deviceDataList = deviceDataRepository.findByTimestampBetweenOrderByTimestampDesc(start, end);
        return deviceDataList.stream()
                .map(deviceDataMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется для быстрого доступа к последним данным.
     */
    @Override
    @Cacheable(value = "latestDeviceData", key = "#deviceId")
    public DeviceDataDto findLatestByDeviceId(Long deviceId) {
        Optional<DeviceData> deviceData = deviceDataRepository.findFirstByDeviceIdOrderByTimestampDesc(deviceId);
        return deviceData.map(deviceDataMapper::toDto).orElse(null);
    }

    // Расширенные методы с пагинацией

    /**
     * {@inheritDoc}
     * 
     * Использует встроенную пагинацию Spring Data для оптимальной производительности.
     */
    @Override
    public Page<DeviceDataDto> findByDeviceId(Long deviceId, Pageable pageable) {
        Page<DeviceData> deviceDataPage = deviceDataRepository.findByDeviceIdOrderByTimestampDesc(deviceId, pageable);
        return deviceDataPage.map(deviceDataMapper::toDto);
    }

    /**
     * {@inheritDoc}
     * 
     * Пагинированный поиск по временному диапазону.
     */
    @Override
    public Page<DeviceDataDto> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        Page<DeviceData> deviceDataPage = deviceDataRepository.findByTimestampBetweenOrderByTimestampDesc(start, end, pageable);
        return deviceDataPage.map(deviceDataMapper::toDto);
    }

    /**
     * {@inheritDoc}
     * 
     * Комбинированный поиск по устройству и времени с пагинацией.
     */
    @Override
    public Page<DeviceDataDto> findByDeviceIdAndTimestampBetween(
            Long deviceId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        Page<DeviceData> deviceDataPage = deviceDataRepository.findByDeviceIdAndTimestampBetweenOrderByTimestampDesc(
            deviceId, start, end, pageable);
        return deviceDataPage.map(deviceDataMapper::toDto);
    }

    /**
     * {@inheritDoc}
     * 
     * Временная реализация возвращает все данные устройства.
     * TODO: Добавить метод с LIMIT в репозиторий.
     */
    @Override
    public List<DeviceDataDto> findLatestDataByDeviceId(Long deviceId, int limit) {
        // Временно возвращаем все данные устройства (без ограничения)
        List<DeviceData> deviceDataList = deviceDataRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
        return deviceDataList.stream()
                .limit(limit)  // Ограничиваем в коде
                .map(deviceDataMapper::toDto)
                .toList();
    }

    // Методы фильтрации по значениям сенсоров

    /**
     * {@inheritDoc}
     * 
     * Использует оптимизированный запрос с диапазонным индексом.
     */
    @Override
    public List<DeviceDataDto> findByTemperatureBetween(Float minTemp, Float maxTemp) {
        List<DeviceData> deviceDataList = deviceDataRepository.findByTemperatureBetweenOrderByTimestampDesc(minTemp, maxTemp);
        return deviceDataList.stream()
                .map(deviceDataMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Поиск по диапазону влажности с сортировкой по времени.
     */
    @Override
    public List<DeviceDataDto> findByHumidityBetween(Float minHumidity, Float maxHumidity) {
        List<DeviceData> deviceDataList = deviceDataRepository.findByHumidityBetweenOrderByTimestampDesc(minHumidity, maxHumidity);
        return deviceDataList.stream()
                .map(deviceDataMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Использует условные запросы для поиска экстремальных значений.
     */
    @Override
    public List<DeviceDataDto> findExtremeTemperatureData(Float threshold, boolean isHigh) {
        List<DeviceData> deviceDataList;
        if (isHigh) {
            deviceDataList = deviceDataRepository.findByTemperatureGreaterThanOrderByTimestampDesc(threshold);
        } else {
            deviceDataList = deviceDataRepository.findByTemperatureLessThanOrderByTimestampDesc(threshold);
        }
        return deviceDataList.stream()
                .map(deviceDataMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Поиск экстремальных значений влажности.
     */
    @Override
    public List<DeviceDataDto> findExtremeHumidityData(Float threshold, boolean isHigh) {
        List<DeviceData> deviceDataList;
        if (isHigh) {
            deviceDataList = deviceDataRepository.findByHumidityGreaterThanOrderByTimestampDesc(threshold);
        } else {
            deviceDataList = deviceDataRepository.findByHumidityLessThanOrderByTimestampDesc(threshold);
        }
        return deviceDataList.stream()
                .map(deviceDataMapper::toDto)
                .toList();
    }

    // Комплексный поиск и фильтрация

    /**
     * {@inheritDoc}
     * 
     * Использует композитные Specifications для гибкого комбинирования фильтров.
     * Все параметры опциональны - null значения игнорируются.
     */
    @Override
    public Page<DeviceDataDto> findDataWithFilters(
            Long deviceId,
            LocalDateTime timestampAfter,
            LocalDateTime timestampBefore,
            Float minTemp,
            Float maxTemp,
            Float minHumidity,
            Float maxHumidity,
            Pageable pageable) {
        
        Page<DeviceData> deviceDataPage = deviceDataRepository.findAll(
            DeviceDataSpecification.buildComplexQuery(
                deviceId, timestampAfter, timestampBefore, 
                minTemp, maxTemp, minHumidity, maxHumidity),
            pageable
        );
        return deviceDataPage.map(deviceDataMapper::toDto);
    }

    // Аналитические и статистические методы

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает null.
     * TODO: Добавить агрегационный запрос в репозиторий.
     */
    @Override
    public Double getAverageTemperature(Long deviceId, LocalDateTime since) {
        // TODO: Реализовать агрегационный запрос
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает null.
     * TODO: Добавить агрегационный запрос в репозиторий.
     */
    @Override
    public Double getAverageHumidity(Long deviceId, LocalDateTime since) {
        // TODO: Реализовать агрегационный запрос
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает пустую карту.
     * TODO: Добавить MIN/MAX агрегации в репозиторий.
     */
    @Override
    public Map<String, Float> getTemperatureRange(Long deviceId, LocalDateTime since) {
        Map<String, Float> range = new HashMap<>();
        // TODO: Реализовать MIN/MAX запросы
        range.put("min", null);
        range.put("max", null);
        return range;
    }

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает пустую карту.
     * TODO: Добавить MIN/MAX агрегации в репозиторий.
     */
    @Override
    public Map<String, Float> getHumidityRange(Long deviceId, LocalDateTime since) {
        Map<String, Float> range = new HashMap<>();
        // TODO: Реализовать MIN/MAX запросы
        range.put("min", null);
        range.put("max", null);
        return range;
    }

    // Методы временной агрегации

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает пустой список.
     * TODO: Добавить PostgreSQL DATE_TRUNC запросы в репозиторий.
     */
    @Override
    public List<Map<String, Object>> getHourlyAverages(Long deviceId, LocalDateTime since) {
        // TODO: Реализовать почасовые агрегации
        return List.of();
    }

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает пустой список.
     * TODO: Добавить дневную статистику в репозиторий.
     */
    @Override
    public List<Map<String, Object>> getDailyStatistics(Long deviceId, LocalDateTime since) {
        // TODO: Реализовать дневную статистику
        return List.of();
    }

    // Методы обнаружения аномалий

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает пустой список.
     * TODO: Добавить метод detectAnomalies в DeviceDataSpecification.
     */
    @Override
    public List<DeviceDataDto> detectAnomalies(
            Long deviceId,
            Float tempThresholdLow,
            Float tempThresholdHigh,
            Float humidityThresholdLow,
            Float humidityThresholdHigh,
            LocalDateTime since) {
        
        // TODO: Реализовать обнаружение аномалий через Specifications
        return List.of();
    }

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает пустой список.
     * TODO: Добавить метод в репозиторий для получения последних данных множества устройств.
     */
    @Override
    public List<DeviceDataDto> getLatestDataForDevices(List<Long> deviceIds, LocalDateTime since) {
        // TODO: Реализовать запрос для множества устройств
        return List.of();
    }

    // Статистические методы

    /**
     * {@inheritDoc}
     * 
     * Использует оптимизированный COUNT запрос.
     */
    @Override
    public long countDataByDevice(Long deviceId) {
        return deviceDataRepository.countByDeviceId(deviceId);
    }

    /**
     * {@inheritDoc}
     * 
     * Подсчет записей после указанной даты.
     */
    @Override
    public long countDataByDeviceAfter(Long deviceId, LocalDateTime after) {
        return deviceDataRepository.countByDeviceIdAndTimestampAfter(deviceId, after);
    }

    /**
     * {@inheritDoc}
     * 
     * Подсчет записей в временном диапазоне.
     */
    @Override
    public long countDataBetween(LocalDateTime start, LocalDateTime end) {
        return deviceDataRepository.countByTimestampBetween(start, end);
    }

    // Методы проверки и очистки данных

    /**
     * {@inheritDoc}
     * 
     * Использует оптимизированный exists запрос.
     */
    @Override
    public boolean hasDataForDevice(Long deviceId) {
        return deviceDataRepository.existsByDeviceId(deviceId);
    }

    /**
     * {@inheritDoc}
     * 
     * Проверка наличия недавних данных.
     */
    @Override
    public boolean hasRecentDataForDevice(Long deviceId, LocalDateTime since) {
        return deviceDataRepository.existsByDeviceIdAndTimestampAfter(deviceId, since);
    }

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает 0.
     * TODO: Добавить метод массового удаления в репозиторий.
     */
    @Override
    @Transactional
    @CacheEvict(value = {"deviceData", "allDeviceData", "deviceDataByDevice", "latestDeviceData"}, allEntries = true)
    public long deleteOldData(LocalDateTime before) {
        // TODO: Реализовать массовое удаление старых записей
        return 0L;
    }

    /**
     * {@inheritDoc}
     * 
     * Временная реализация - возвращает 0.
     * TODO: Добавить метод удаления старых записей устройства в репозиторий.
     */
    @Override
    @Transactional
    @CacheEvict(value = {"deviceData", "allDeviceData", "deviceDataByDevice", "latestDeviceData"}, allEntries = true)
    public long deleteOldDataForDevice(Long deviceId, LocalDateTime before) {
        // TODO: Реализовать удаление старых записей конкретного устройства
        return 0L;
    }
} 