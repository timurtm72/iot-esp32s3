package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.LedStripDataDto;
import dev.timur.example.iotesp32s3.service.LedStripDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "LED Strip Data API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/led-strip-data")
public class LedStripDataController {

    private final LedStripDataService service;

    @Operation(summary = "Создать запись")
    @PostMapping
    public ResponseEntity<LedStripDataDto> create(@RequestBody LedStripDataDto dto) {
        LedStripDataDto created = service.createLedStripData(dto);
        return ResponseEntity.created(URI.create("/api/led-strip-data/" + created.getId())).body(created);
    }

    @Operation(summary = "Получить запись по ID")
    @GetMapping("/{id}")
    public ResponseEntity<LedStripDataDto> getById(@PathVariable Long id) {
        return service.getLedStripDataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Обновить запись")
    @PutMapping("/{id}")
    public ResponseEntity<LedStripDataDto> update(@PathVariable Long id, @RequestBody LedStripDataDto dto) {
        return service.updateLedStripData(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить запись")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.deleteLedStripData(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Все данные")
    @GetMapping
    public List<LedStripDataDto> findAll() {
        return service.getAllLedStripData();
    }

    @Operation(summary = "По устройству и диапазону дат")
    @GetMapping("/device/{deviceId}/range")
    public List<LedStripDataDto> byDeviceAndRange(@PathVariable Long deviceId,
                                                  @RequestParam String start,
                                                  @RequestParam String end) {
        return service.getLedStripDataByDeviceAndTimeRange(deviceId, LocalDateTime.parse(start), LocalDateTime.parse(end));
    }
} 