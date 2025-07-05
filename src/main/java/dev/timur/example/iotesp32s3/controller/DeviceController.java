package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Device API", description = "CRUD операций для устройств")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "Создать устройство")
    @PostMapping
    public ResponseEntity<DeviceDto> create(@RequestBody DeviceDto dto) {
        DeviceDto created = deviceService.createDevice(dto);
        return ResponseEntity.created(URI.create("/api/devices/" + created.getId())).body(created);
    }

    @Operation(summary = "Получить устройство по ID")
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getById(@PathVariable Long id) {
        return deviceService.getDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Обновить устройство")
    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> update(@PathVariable Long id, @RequestBody DeviceDto dto) {
        return deviceService.updateDevice(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить устройство (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return deviceService.deleteDevice(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Получить все активные устройства")
    @GetMapping
    public List<DeviceDto> findAll() {
        return deviceService.getAllActiveDevices();
    }

    // Дополнительный пример запроса
    @Operation(summary = "Получить устройства, созданные после указанной даты")
    @GetMapping("/created-after/{isoDateTime}")
    public List<DeviceDto> createdAfter(@PathVariable String isoDateTime) {
        return deviceService.getDevicesCreatedAfter(LocalDateTime.parse(isoDateTime));
    }
} 