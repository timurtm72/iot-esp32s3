package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDto;
import dev.timur.example.iotesp32s3.service.TempAndHumidityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * REST контроллер для управления устройствами температуры и влажности
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/temp-humidity")
@RequiredArgsConstructor
@Validated
public class TempAndHumidityController {

    private final TempAndHumidityService tempAndHumidityService;

    /**
     * Создать новое устройство температуры и влажности
     * @param deviceDto данные устройства
     * @return созданное устройство
     */
    @PostMapping
    public ResponseEntity<TempAndHumidityDto> createDevice(
            @Valid @RequestBody TempAndHumidityDto deviceDto) {
        log.info("POST /api/v1/temp-humidity - создание устройства: {}", deviceDto.getName());
        
        TempAndHumidityDto createdDevice = tempAndHumidityService.createDevice(deviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
    }

    /**
     * Получить устройство по ID
     * @param id идентификатор устройства
     * @return устройство
     */
    @GetMapping("/{id}")
    public ResponseEntity<TempAndHumidityDto> getDeviceById(
            @PathVariable @NotNull Long id) {
        log.debug("GET /api/v1/temp-humidity/{} - получение устройства", id);
        
        TempAndHumidityDto device = tempAndHumidityService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    /**
     * Получить все устройства пользователя
     * @param ownerId идентификатор владельца
     * @return список устройств
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<TempAndHumidityDto>> getDevicesByOwnerId(
            @PathVariable @NotNull Long ownerId) {
        log.debug("GET /api/v1/temp-humidity/owner/{} - получение устройств пользователя", ownerId);
        
        List<TempAndHumidityDto> devices = tempAndHumidityService.getDevicesByOwnerId(ownerId);
        return ResponseEntity.ok(devices);
    }

    /**
     * Получить все активные устройства
     * @return список активных устройств
     */
    @GetMapping
    public ResponseEntity<List<TempAndHumidityDto>> getAllActiveDevices() {
        log.debug("GET /api/v1/temp-humidity - получение всех активных устройств");
        
        List<TempAndHumidityDto> devices = tempAndHumidityService.getAllActiveDevices();
        return ResponseEntity.ok(devices);
    }

    /**
     * Обновить устройство
     * @param id идентификатор устройства
     * @param deviceDto новые данные устройства
     * @return обновленное устройство
     */
    @PutMapping("/{id}")
    public ResponseEntity<TempAndHumidityDto> updateDevice(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody TempAndHumidityDto deviceDto) {
        log.info("PUT /api/v1/temp-humidity/{} - обновление устройства", id);
        
        TempAndHumidityDto updatedDevice = tempAndHumidityService.updateDevice(id, deviceDto);
        return ResponseEntity.ok(updatedDevice);
    }

    /**
     * Удалить устройство
     * @param id идентификатор устройства
     * @return статус операции
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(
            @PathVariable @NotNull Long id) {
        log.info("DELETE /api/v1/temp-humidity/{} - удаление устройства", id);
        
        tempAndHumidityService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Проверить существование устройства
     * @param id идентификатор устройства
     * @return результат проверки
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(
            @PathVariable @NotNull Long id) {
        log.debug("GET /api/v1/temp-humidity/{}/exists - проверка существования устройства", id);
        
        boolean exists = tempAndHumidityService.existsById(id);
        return ResponseEntity.ok(exists);
    }
} 