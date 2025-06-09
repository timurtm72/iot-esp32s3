package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.LedStripDto;
import dev.timur.example.iotesp32s3.mapper.LedStripMapper;
import dev.timur.example.iotesp32s3.model.LedStrip;
import dev.timur.example.iotesp32s3.model.User;
import dev.timur.example.iotesp32s3.repository.LedStripRepository;
import dev.timur.example.iotesp32s3.repository.UserRepository;
import dev.timur.example.iotesp32s3.service.LedStripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса для работы с LED лентами
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LedStripServiceImpl implements LedStripService {

    private final LedStripRepository ledStripRepository;
    private final UserRepository userRepository;
    private final LedStripMapper mapper;

    /**
     * Создать новое устройство LED ленты
     * @param deviceDto данные устройства
     * @return созданное устройство
     */
    @Override
    @Transactional
    public LedStripDto createDevice(LedStripDto deviceDto) {
        log.info("Создание нового устройства LED ленты: {}", deviceDto.getName());
        
        // Получаем владельца по ID
        User owner = userRepository.findById(deviceDto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: " + deviceDto.getOwnerId()));
        
        // Преобразуем DTO в entity
        LedStrip device = mapper.toEntity(deviceDto);
        device.setOwner(owner);
        
        // Сохраняем устройство
        LedStrip savedDevice = ledStripRepository.save(device);
        
        log.info("Устройство LED ленты создано с ID: {}", savedDevice.getId());
        return mapper.toDto(savedDevice);
    }

    /**
     * Получить устройство по ID
     * @param id идентификатор устройства
     * @return устройство
     */
    @Override
    public LedStripDto getDeviceById(Long id) {
        log.debug("Получение устройства LED ленты по ID: {}", id);
        
        LedStrip device = ledStripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено с ID: " + id));
        
        return mapper.toDto(device);
    }

    /**
     * Получить все устройства пользователя
     * @param ownerId идентификатор владельца
     * @return список устройств
     */
    @Override
    public List<LedStripDto> getDevicesByOwnerId(Long ownerId) {
        log.debug("Получение устройств LED ленты пользователя с ID: {}", ownerId);
        
        List<LedStrip> devices = ledStripRepository.findByOwnerIdAndRemovedAtIsNull(ownerId);
        return mapper.toDtoList(devices);
    }

    /**
     * Получить все активные устройства
     * @return список активных устройств
     */
    @Override
    public List<LedStripDto> getAllActiveDevices() {
        log.debug("Получение всех активных устройств LED ленты");
        
        List<LedStrip> devices = ledStripRepository.findByRemovedAtIsNull();
        return mapper.toDtoList(devices);
    }

    /**
     * Обновить устройство
     * @param id идентификатор устройства
     * @param deviceDto новые данные устройства
     * @return обновленное устройство
     */
    @Override
    @Transactional
    public LedStripDto updateDevice(Long id, LedStripDto deviceDto) {
        log.info("Обновление устройства LED ленты с ID: {}", id);
        
        LedStrip existingDevice = ledStripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено с ID: " + id));
        
        // Обновляем поля устройства
        mapper.updateEntityFromDto(deviceDto, existingDevice);
        
        // Сохраняем изменения
        LedStrip updatedDevice = ledStripRepository.save(existingDevice);
        
        log.info("Устройство LED ленты обновлено с ID: {}", updatedDevice.getId());
        return mapper.toDto(updatedDevice);
    }

    /**
     * Удалить устройство (мягкое удаление)
     * @param id идентификатор устройства
     */
    @Override
    @Transactional
    public void deleteDevice(Long id) {
        log.info("Мягкое удаление устройства LED ленты с ID: {}", id);
        
        LedStrip device = ledStripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено с ID: " + id));
        
        // Устанавливаем время удаления
        device.setRemovedAt(LocalDateTime.now());
        ledStripRepository.save(device);
        
        log.info("Устройство LED ленты помечено как удаленное с ID: {}", id);
    }

    /**
     * Проверить существование устройства
     * @param id идентификатор устройства
     * @return true если устройство существует
     */
    @Override
    public boolean existsById(Long id) {
        log.debug("Проверка существования устройства LED ленты с ID: {}", id);
        
        return ledStripRepository.existsById(id);
    }
} 