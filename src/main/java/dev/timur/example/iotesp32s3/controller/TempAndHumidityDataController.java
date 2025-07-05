package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDataDto;
import dev.timur.example.iotesp32s3.service.TempAndHumidityDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Temperature & Humidity Data API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/temp-humidity-data")
public class TempAndHumidityDataController {

    private final TempAndHumidityDataService service;

    @Operation(summary = "Создать запись")
    @PostMapping
    public ResponseEntity<TempAndHumidityDataDto> create(@RequestBody TempAndHumidityDataDto dto) {
        TempAndHumidityDataDto created = service.createTempAndHumidityData(dto);
        return ResponseEntity.created(URI.create("/api/temp-humidity-data/" + created.getId())).body(created);
    }

    @Operation(summary = "Получить запись по ID")
    @GetMapping("/{id}")
    public ResponseEntity<TempAndHumidityDataDto> getById(@PathVariable Long id) {
        return service.getTempAndHumidityDataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Обновить запись")
    @PutMapping("/{id}")
    public ResponseEntity<TempAndHumidityDataDto> update(@PathVariable Long id, @RequestBody TempAndHumidityDataDto dto) {
        return service.updateTempAndHumidityData(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить запись")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.deleteTempAndHumidityData(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Все данные")
    @GetMapping
    public List<TempAndHumidityDataDto> findAll() {
        return service.getAllTempAndHumidityData();
    }

    @Operation(summary = "По устройству и диапазону дат")
    @GetMapping("/device/{deviceId}/range")
    public List<TempAndHumidityDataDto> byDeviceAndRange(@PathVariable Long deviceId,
                                                         @RequestParam String start,
                                                         @RequestParam String end) {
        return service.getTempAndHumidityDataByDeviceAndTimeRange(deviceId, LocalDateTime.parse(start), LocalDateTime.parse(end));
    }
} 