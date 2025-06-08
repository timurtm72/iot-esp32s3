package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.mapper.DeviceMapper;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.service.DeviceService;
import dev.timur.example.iotesp32s3.specification.DeviceSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для управления IoT устройствами в системе ESP32S3.
 * Предоставляет полный набор операций CRUD с поддержкой мягкого удаления,
 * кеширования, современных методов поиска и фильтрации через Specifications.
 * 
 * Особенности:
 * - Поддержка мягкого удаления устройств через поле removedAt
 * - Кеширование частых запросов для оптимизации производительности  
 * - Интеграция с системой телеметрии для мониторинга активности устройств
 * - Современные методы поиска с пагинацией и фильтрацией
 * - Транзакционная безопасность для операций изменения данных
 */
@Service
public class DeviceServiceImpl implements DeviceService {
    
    /** Репозиторий для работы с данными устройств */
    private final DeviceRepository deviceRepository;
    
    /** Маппер для преобразования между DTO и Entity */
    private final DeviceMapper deviceMapper;

    /**
     * Конструктор сервиса с внедрением зависимостей.
     * 
     * @param deviceRepository репозиторий для работы с устройствами
     * @param deviceMapper маппер для преобразования объектов
     */
    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется для оптимизации повторных запросов.
     * Использует мягкое удаление - возвращает только активные устройства.
     */
    @Transactional
    @Override
    @Cacheable(value = "devices", key = "#id")
    public DeviceDto getById(Long id) {
        Optional<Device> device = deviceRepository.findByIdAndRemovedAtIsNull(id);
        return device.map(deviceMapper::toDto).orElse(null);
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется для оптимизации повторных запросов.
     * Возвращает только активные (не удаленные) устройства.
     */
    @Transactional
    @Override
    @Cacheable(value = "allDevices")
    public List<DeviceDto> getAll() {
        List<Device> devices = deviceRepository.findByRemovedAtIsNull();
        return devices.stream()
                .map(deviceMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Выполняется в транзакции и очищает связанные кеши.
     * Устанавливает null для поля dataValues при создании.
     */
    @Transactional
    @Override
    @CacheEvict(value = {"devices", "allDevices", "devicesByName", "devicesByLocation"}, allEntries = true)
    public Status create(DeviceDto deviceDto) {
        if (deviceDto == null) {
            return Status.IS_EMPTY;
        }
        Device newDevice = deviceMapper.toEntity(deviceDto);
        newDevice.setDataValues(null);
        deviceRepository.save(newDevice);
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
    @CacheEvict(value = {"devices", "allDevices", "devicesByName", "devicesByLocation"}, allEntries = true)
    public Status update(DeviceDto deviceDto, Long id) {
        if (deviceDto == null) {
            return Status.IS_EMPTY;
        }
        
        Optional<Device> updateDeviceOpt = deviceRepository.findByIdAndRemovedAtIsNull(id);
        if (updateDeviceOpt.isEmpty()) {
            return Status.IS_NOT_FOUND;
        }
        Device updateDevice = updateDeviceOpt.get();

        // Обновляем только непустые поля
        if (deviceDto.getName() != null && !deviceDto.getName().isEmpty()) {
            updateDevice.setName(deviceDto.getName());
        }

        if (deviceDto.getDescription() != null && !deviceDto.getDescription().isEmpty()) {
            updateDevice.setDescription(deviceDto.getDescription());
        }

        if (deviceDto.getLocation() != null && !deviceDto.getLocation().isEmpty()) {
            updateDevice.setLocation(deviceDto.getLocation());
        }

        deviceRepository.save(updateDevice);
        return Status.IS_OK;
    }

    /**
     * {@inheritDoc}
     * 
     * Выполняется в транзакции и очищает связанные кеши.
     * Выполняет жесткое удаление - в production следует использовать мягкое удаление.
     */
    @Transactional
    @Override
    @CacheEvict(value = {"devices", "allDevices", "devicesByName", "devicesByLocation"}, allEntries = true)
    public Status delete(Long id) {
        Optional<Device> removeDevice = deviceRepository.findByIdAndRemovedAtIsNull(id);
        if (removeDevice.isEmpty()) {
            return Status.IS_NULL;
        }
        deviceRepository.deleteById(id);
        return Status.IS_OK;
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется по названию для оптимизации.
     * Использует оптимизированный Query Method с ILIKE поиском.
     */
    @Override
    @Cacheable(value = "devicesByName", key = "#name")
    public List<DeviceDto> findByNameContaining(String name) {
        List<Device> devices = deviceRepository.findByNameContainingIgnoreCaseAndRemovedAtIsNull(name);
        return devices.stream()
                .map(deviceMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется по местоположению для оптимизации.
     */
    @Override
    @Cacheable(value = "devicesByLocation", key = "#location")
    public List<DeviceDto> findByLocationContaining(String location) {
        List<Device> devices = deviceRepository.findByLocationContainingIgnoreCaseAndRemovedAtIsNull(location);
        return devices.stream()
                .map(deviceMapper::toDto)
                .toList();
    }

    // Расширенные методы с пагинацией

    /**
     * {@inheritDoc}
     * 
     * Использует встроенную пагинацию Spring Data для оптимальной производительности.
     */
    @Override
    public Page<DeviceDto> getAllActive(Pageable pageable) {
        Page<Device> devices = deviceRepository.findByRemovedAtIsNull(pageable);
        return devices.map(deviceMapper::toDto);
    }

    /**
     * {@inheritDoc}
     * 
     * Пагинированный поиск с кешированием.
     */
    @Override
    public Page<DeviceDto> findByNameContaining(String name, Pageable pageable) {
        Page<Device> devices = deviceRepository.findByNameContainingIgnoreCaseAndRemovedAtIsNull(name, pageable);
        return devices.map(deviceMapper::toDto);
    }

    /**
     * {@inheritDoc}
     * 
     * Пагинированный поиск по местоположению.
     */
    @Override
    public Page<DeviceDto> findByLocationContaining(String location, Pageable pageable) {
        Page<Device> devices = deviceRepository.findByLocationContainingIgnoreCaseAndRemovedAtIsNull(location, pageable);
        return devices.map(deviceMapper::toDto);
    }

    /**
     * {@inheritDoc}
     * 
     * Использует нативный SQL запрос для полнотекстового поиска.
     */
    @Override
    public List<DeviceDto> searchDevices(String searchText) {
        List<Device> devices = deviceRepository.searchActiveDevices(searchText);
        return devices.stream()
                .map(deviceMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Пагинированный полнотекстовый поиск.
     */
    @Override
    public Page<DeviceDto> searchDevices(String searchText, Pageable pageable) {
        Page<Device> devices = deviceRepository.searchActiveDevices(searchText, pageable);
        return devices.map(deviceMapper::toDto);
    }

    // Методы фильтрации по времени

    /**
     * {@inheritDoc}
     * 
     * Использует Query Method для оптимизированного поиска по дате.
     */
    @Override
    public List<DeviceDto> findDevicesCreatedAfter(LocalDateTime date) {
        List<Device> devices = deviceRepository.findByCreatedAtAfterAndRemovedAtIsNull(date);
        return devices.stream()
                .map(deviceMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Использует диапазонный поиск по дате создания.
     */
    @Override
    public List<DeviceDto> findDevicesCreatedBetween(LocalDateTime start, LocalDateTime end) {
        List<Device> devices = deviceRepository.findByCreatedAtBetweenAndRemovedAtIsNull(start, end);
        return devices.stream()
                .map(deviceMapper::toDto)
                .toList();
    }

    // Комплексный поиск с фильтрами

    /**
     * {@inheritDoc}
     * 
     * Использует композитные Specifications для гибкого комбинирования фильтров.
     * Все параметры опциональны - null значения игнорируются.
     */
    @Override
    public Page<DeviceDto> findDevicesWithFilters(
            String searchText,
            String name,
            String location,
            String description,
            LocalDateTime createdAfter,
            LocalDateTime createdBefore,
            Pageable pageable) {
        
        Page<Device> devices = deviceRepository.findAll(
            DeviceSpecification.buildComplexQuery(searchText, name, location, description, createdAfter, createdBefore, true),
            pageable
        );
        return devices.map(deviceMapper::toDto);
    }

    // Интеграция с телеметрией

    /**
     * {@inheritDoc}
     * 
     * Временная реализация через простое получение устройства.
     * TODO: Добавить специальный метод в репозиторий для загрузки с данными.
     */
    @Override
    public DeviceDto getDeviceWithLatestData(Long id) {
        // Временно используем обычное получение устройства
        return getById(id);
    }

    /**
     * {@inheritDoc}
     * 
     * Временная реализация возвращает все активные устройства.
     * TODO: Добавить метод в репозиторий для поиска устройств с недавними данными.
     */
    @Override
    public List<DeviceDto> findDevicesWithRecentData(LocalDateTime since) {
        // Временно возвращаем все активные устройства
        return getAll();
    }

    // Проверки существования

    /**
     * {@inheritDoc}
     * 
     * Использует оптимизированный exists запрос вместо count.
     */
    @Override
    public boolean existsByName(String name) {
        return deviceRepository.existsByNameAndRemovedAtIsNull(name);
    }

    /**
     * {@inheritDoc}
     * 
     * Проверка уникальности при обновлении устройства.
     */
    @Override
    public boolean existsByNameAndIdNot(String name, Long excludeId) {
        return deviceRepository.existsByNameAndIdNotAndRemovedAtIsNull(name, excludeId);
    }

    // Статистические методы

    /**
     * {@inheritDoc}
     * 
     * Подсчет только активных устройств с использованием оптимизированного запроса.
     */
    @Override
    public long countActiveDevices() {
        return deviceRepository.countByRemovedAtIsNull();
    }

    /**
     * {@inheritDoc}
     * 
     * Подсчет активных устройств, созданных после указанной даты.
     */
    @Override
    public long countDevicesCreatedAfter(LocalDateTime date) {
        return deviceRepository.countByCreatedAtAfterAndRemovedAtIsNull(date);
    }

    /**
     * {@inheritDoc}
     * 
     * Подсчет активных устройств с названием, содержащим указанный текст.
     */
    @Override
    public long countDevicesByNameContaining(String name) {
        return deviceRepository.countByNameContainingIgnoreCaseAndRemovedAtIsNull(name);
    }
} 