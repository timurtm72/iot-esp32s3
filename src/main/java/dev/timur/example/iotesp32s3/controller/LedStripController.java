package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.LedStripDto;
import dev.timur.example.iotesp32s3.service.LedStripService;
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
 * REST контроллер для управления LED лентами
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/led-strip")
@RequiredArgsConstructor
@Validated
public class LedStripController {

    private final LedStripService ledStripService;

    /**
     * Создать новое устройство LED ленты
     * @param deviceDto данные устройства
     * @return созданное устройство
     */
    @PostMapping
    public ResponseEntity<LedStripDto> createDevice(
            @Valid @RequestBody LedStripDto deviceDto) {
        log.info("POST /api/v1/led-strip - создание LED устройства: {}", deviceDto.getName());
        
        LedStripDto createdDevice = ledStripService.createDevice(deviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
    }

    /**
     * Получить устройство по ID
     * @param id идентификатор устройства
     * @return устройство
     */
    @GetMapping("/{id}")
    public ResponseEntity<LedStripDto> getDeviceById(
            @PathVariable @NotNull Long id) {
        log.debug("GET /api/v1/led-strip/{} - получение LED устройства", id);
        
        LedStripDto device = ledStripService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    /**
     * Получить все устройства пользователя
     * @param ownerId идентификатор владельца
     * @return список устройств
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<LedStripDto>> getDevicesByOwnerId(
            @PathVariable @NotNull Long ownerId) {
        log.debug("GET /api/v1/led-strip/owner/{} - получение LED устройств пользователя", ownerId);
        
        List<LedStripDto> devices = ledStripService.getDevicesByOwnerId(ownerId);
        return ResponseEntity.ok(devices);
    }

    /**
     * Получить все активные устройства
     * @return список активных устройств
     */
    @GetMapping
    public ResponseEntity<List<LedStripDto>> getAllActiveDevices() {
        log.debug("GET /api/v1/led-strip - получение всех активных LED устройств");
        
        List<LedStripDto> devices = ledStripService.getAllActiveDevices();
        return ResponseEntity.ok(devices);
    }

    /**
     * Обновить устройство
     * @param id идентификатор устройства
     * @param deviceDto новые данные устройства
     * @return обновленное устройство
     */
    @PutMapping("/{id}")
    public ResponseEntity<LedStripDto> updateDevice(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody LedStripDto deviceDto) {
        log.info("PUT /api/v1/led-strip/{} - обновление LED устройства", id);
        
        LedStripDto updatedDevice = ledStripService.updateDevice(id, deviceDto);
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
        log.info("DELETE /api/v1/led-strip/{} - удаление LED устройства", id);
        
        ledStripService.deleteDevice(id);
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
        log.debug("GET /api/v1/led-strip/{}/exists - проверка существования LED устройства", id);
        
        boolean exists = ledStripService.existsById(id);
        return ResponseEntity.ok(exists);
    }
} 