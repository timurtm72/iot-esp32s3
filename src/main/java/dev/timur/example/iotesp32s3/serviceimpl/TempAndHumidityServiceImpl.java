package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDto;
import dev.timur.example.iotesp32s3.mapper.TempAndHumidityMapper;
import dev.timur.example.iotesp32s3.model.TempAndHumidity;
import dev.timur.example.iotesp32s3.model.User;
import dev.timur.example.iotesp32s3.repository.TempAndHumidityRepository;
import dev.timur.example.iotesp32s3.repository.UserRepository;
import dev.timur.example.iotesp32s3.service.TempAndHumidityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса для работы с устройствами температуры и влажности
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TempAndHumidityServiceImpl implements TempAndHumidityService {

    private final TempAndHumidityRepository tempAndHumidityRepository;
    private final UserRepository userRepository;
    private final TempAndHumidityMapper mapper;

    /**
     * Создать новое устройство температуры и влажности
     * @param deviceDto данные устройства
     * @return созданное устройство
     */
    @Override
    @Transactional
    public TempAndHumidityDto createDevice(TempAndHumidityDto deviceDto) {
        log.info("Создание нового устройства температуры и влажности: {}", deviceDto.getName());
        
        // Получаем владельца по ID
        User owner = userRepository.findById(deviceDto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: " + deviceDto.getOwnerId()));
        
        // Преобразуем DTO в entity
        TempAndHumidity device = mapper.toEntity(deviceDto);
        device.setOwner(owner);
        
        // Сохраняем устройство
        TempAndHumidity savedDevice = tempAndHumidityRepository.save(device);
        
        log.info("Устройство температуры и влажности создано с ID: {}", savedDevice.getId());
        return mapper.toDto(savedDevice);
    }

    /**
     * Получить устройство по ID
     * @param id идентификатор устройства
     * @return устройство
     */
    @Override
    public TempAndHumidityDto getDeviceById(Long id) {
        log.debug("Получение устройства температуры и влажности по ID: {}", id);
        
        TempAndHumidity device = tempAndHumidityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено с ID: " + id));
        
        return mapper.toDto(device);
    }

    /**
     * Получить все устройства пользователя
     * @param ownerId идентификатор владельца
     * @return список устройств
     */
    @Override
    public List<TempAndHumidityDto> getDevicesByOwnerId(Long ownerId) {
        log.debug("Получение устройств температуры и влажности пользователя с ID: {}", ownerId);
        
        List<TempAndHumidity> devices = tempAndHumidityRepository.findByOwnerIdAndRemovedAtIsNull(ownerId);
        return mapper.toDtoList(devices);
    }

    /**
     * Получить все активные устройства
     * @return список активных устройств
     */
    @Override
    public List<TempAndHumidityDto> getAllActiveDevices() {
        log.debug("Получение всех активных устройств температуры и влажности");
        
        List<TempAndHumidity> devices = tempAndHumidityRepository.findByRemovedAtIsNull();
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
    public TempAndHumidityDto updateDevice(Long id, TempAndHumidityDto deviceDto) {
        log.info("Обновление устройства температуры и влажности с ID: {}", id);
        
        TempAndHumidity existingDevice = tempAndHumidityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено с ID: " + id));
        
        // Обновляем поля устройства
        mapper.updateEntityFromDto(deviceDto, existingDevice);
        
        // Сохраняем изменения
        TempAndHumidity updatedDevice = tempAndHumidityRepository.save(existingDevice);
        
        log.info("Устройство температуры и влажности обновлено с ID: {}", updatedDevice.getId());
        return mapper.toDto(updatedDevice);
    }

    /**
     * Удалить устройство (мягкое удаление)
     * @param id идентификатор устройства
     */
    @Override
    @Transactional
    public void deleteDevice(Long id) {
        log.info("Мягкое удаление устройства температуры и влажности с ID: {}", id);
        
        TempAndHumidity device = tempAndHumidityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено с ID: " + id));
        
        // Устанавливаем время удаления
        device.setRemovedAt(LocalDateTime.now());
        tempAndHumidityRepository.save(device);
        
        log.info("Устройство температуры и влажности помечено как удаленное с ID: {}", id);
    }

    /**
     * Проверить существование устройства
     * @param id идентификатор устройства
     * @return true если устройство существует
     */
    @Override
    public boolean existsById(Long id) {
        log.debug("Проверка существования устройства температуры и влажности с ID: {}", id);
        
        return tempAndHumidityRepository.existsById(id);
    }
} 