package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.mapper.DeviceMapper;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.model.User;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.repository.UserRepository;
import dev.timur.example.iotesp32s3.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с устройствами IoT
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceServiceImpl implements DeviceService {
    
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final DeviceMapper deviceMapper;
    
    /**
     * Создание нового устройства
     * @param deviceDto данные устройства
     * @return созданное устройство
     */
    @Override
    @Transactional
    public DeviceDto createDevice(DeviceDto deviceDto) {
        log.info("Создание нового устройства с названием: {}", deviceDto.getName());
        
        Device device = deviceMapper.toEntity(deviceDto);
        
        // Установка владельца устройства
        if (deviceDto.getOwnerId() != null) {
            Optional<User> owner = userRepository.findById(deviceDto.getOwnerId());
            if (owner.isPresent()) {
                device.setOwner(owner.get());
            } else {
                log.warn("Пользователь с ID {} не найден", deviceDto.getOwnerId());
                throw new IllegalArgumentException("Владелец устройства не найден");
            }
        }
        
        Device savedDevice = deviceRepository.save(device);
        log.info("Устройство успешно создано с ID: {}", savedDevice.getId());
        
        return deviceMapper.toDto(savedDevice);
    }
    
    /**
     * Получение устройства по идентификатору
     * @param id идентификатор устройства
     * @return устройство или пустой Optional
     */
    @Override
    @Cacheable(value = "devices", key = "#id")
    public Optional<DeviceDto> getDeviceById(Long id) {
        log.debug("Поиск устройства по ID: {}", id);
        
        return deviceRepository.findById(id)
                .filter(device -> device.getRemovedAt() == null)
                .map(deviceMapper::toDto);
    }
    
    /**
     * Обновление данных устройства
     * @param id идентификатор устройства
     * @param deviceDto новые данные устройства
     * @return обновленное устройство
     */
    @Override
    @Transactional
    public Optional<DeviceDto> updateDevice(Long id, DeviceDto deviceDto) {
        log.info("Обновление устройства с ID: {}", id);
        
        return deviceRepository.findById(id)
                .filter(device -> device.getRemovedAt() == null)
                .map(existingDevice -> {
                    deviceMapper.updateEntity(deviceDto, existingDevice);
                    
                    // Обновление владельца если изменился
                    if (deviceDto.getOwnerId() != null) {
                        Optional<User> newOwner = userRepository.findById(deviceDto.getOwnerId());
                        if (newOwner.isPresent()) {
                            existingDevice.setOwner(newOwner.get());
                        }
                    }
                    
                    Device updatedDevice = deviceRepository.save(existingDevice);
                    log.info("Устройство с ID {} успешно обновлено", id);
                    
                    return deviceMapper.toDto(updatedDevice);
                });
    }
    
    /**
     * Мягкое удаление устройства
     * @param id идентификатор устройства
     * @return true если устройство удалено, false если не найдено
     */
    @Override
    @Transactional
    public boolean deleteDevice(Long id) {
        log.info("Удаление устройства с ID: {}", id);
        
        return deviceRepository.findById(id)
                .filter(device -> device.getRemovedAt() == null)
                .map(device -> {
                    device.setRemovedAt(LocalDateTime.now());
                    deviceRepository.save(device);
                    log.info("Устройство с ID {} успешно удалено", id);
                    return true;
                })
                .orElse(false);
    }
    
    /**
     * Получение всех активных устройств
     * @return список активных устройств
     */
    @Override
    @Cacheable("activeDevices")
    public List<DeviceDto> getAllActiveDevices() {
        log.debug("Получение всех активных устройств");
        
        List<Device> devices = deviceRepository.findByRemovedAtIsNull();
        return deviceMapper.toDtoList(devices);
    }
    
    /**
     * Поиск устройства по названию
     * @param name название устройства
     * @return устройство или пустой Optional
     */
    @Override
    public Optional<DeviceDto> getDeviceByName(String name) {
        log.debug("Поиск устройства по названию: {}", name);
        
        return deviceRepository.findByNameAndRemovedAtIsNull(name)
                .map(deviceMapper::toDto);
    }
    
    /**
     * Получение устройств пользователя
     * @param ownerId идентификатор владельца
     * @return список устройств пользователя
     */
    @Override
    @Cacheable(value = "userDevices", key = "#ownerId")
    public List<DeviceDto> getDevicesByOwner(Long ownerId) {
        log.debug("Получение устройств пользователя с ID: {}", ownerId);
        
        List<Device> devices = deviceRepository.findByOwnerIdAndRemovedAtIsNull(ownerId);
        return deviceMapper.toDtoList(devices);
    }
    
    /**
     * Получение устройств по городу
     * @param city город
     * @return список устройств
     */
    @Override
    public List<DeviceDto> getDevicesByCity(String city) {
        log.debug("Получение устройств по городу: {}", city);
        List<Device> devices = deviceRepository.findByLocation_CityAndRemovedAtIsNull(city);
        return deviceMapper.toDtoList(devices);
    }
    
    /**
     * Получение устройств созданных после указанной даты
     * @param createdAfter дата создания
     * @return список устройств
     */
    @Override
    public List<DeviceDto> getDevicesCreatedAfter(LocalDateTime createdAfter) {
        log.debug("Получение устройств созданных после: {}", createdAfter);
        
        List<Device> devices = deviceRepository.findByCreatedAtAfterAndRemovedAtIsNull(createdAfter);
        return deviceMapper.toDtoList(devices);
    }
} 