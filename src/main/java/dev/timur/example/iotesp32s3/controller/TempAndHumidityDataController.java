package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.TempAndHumidityDataDto;
import dev.timur.example.iotesp32s3.service.TempAndHumidityDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST контроллер для управления данными датчиков температуры и влажности
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/temp-humidity-data")
@RequiredArgsConstructor
@Validated
public class TempAndHumidityDataController {

    private final TempAndHumidityDataService dataService;

    /**
     * Создать новую запись данных
     * @param dataDto данные с датчиков
     * @return созданная запись
     */
    @PostMapping
    public ResponseEntity<TempAndHumidityDataDto> createData(
            @Valid @RequestBody TempAndHumidityDataDto dataDto) {
        log.info("POST /api/v1/temp-humidity-data - создание записи данных для устройства ID: {}", dataDto.getDeviceId());
        
        TempAndHumidityDataDto createdData = dataService.createData(dataDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdData);
    }

    /**
     * Получить запись по ID
     * @param id идентификатор записи
     * @return запись данных
     */
    @GetMapping("/{id}")
    public ResponseEntity<TempAndHumidityDataDto> getDataById(
            @PathVariable @NotNull Long id) {
        log.debug("GET /api/v1/temp-humidity-data/{} - получение записи данных", id);
        
        TempAndHumidityDataDto data = dataService.getDataById(id);
        return ResponseEntity.ok(data);
    }

    /**
     * Получить все данные устройства
     * @param deviceId идентификатор устройства
     * @return список данных
     */
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<TempAndHumidityDataDto>> getDataByDeviceId(
            @PathVariable @NotNull Long deviceId) {
        log.debug("GET /api/v1/temp-humidity-data/device/{} - получение всех данных устройства", deviceId);
        
        List<TempAndHumidityDataDto> dataList = dataService.getDataByDeviceId(deviceId);
        return ResponseEntity.ok(dataList);
    }

    /**
     * Получить данные за период
     * @param deviceId идентификатор устройства
     * @param startTime начало периода
     * @param endTime конец периода
     * @return список данных за период
     */
    @GetMapping("/device/{deviceId}/period")
    public ResponseEntity<List<TempAndHumidityDataDto>> getDataByPeriod(
            @PathVariable @NotNull Long deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        log.debug("GET /api/v1/temp-humidity-data/device/{}/period - получение данных за период с {} по {}", 
                deviceId, startTime, endTime);
        
        List<TempAndHumidityDataDto> dataList = dataService.getDataByPeriod(deviceId, startTime, endTime);
        return ResponseEntity.ok(dataList);
    }

    /**
     * Получить последние данные устройства
     * @param deviceId идентификатор устройства
     * @param limit количество записей
     * @return последние записи
     */
    @GetMapping("/device/{deviceId}/latest")
    public ResponseEntity<List<TempAndHumidityDataDto>> getLatestData(
            @PathVariable @NotNull Long deviceId,
            @RequestParam(defaultValue = "10") @Positive int limit) {
        log.debug("GET /api/v1/temp-humidity-data/device/{}/latest - получение последних {} записей", deviceId, limit);
        
        List<TempAndHumidityDataDto> dataList = dataService.getLatestData(deviceId, limit);
        return ResponseEntity.ok(dataList);
    }

    /**
     * Получить данные с высокой температурой
     * @param deviceId идентификатор устройства
     * @param minTemperature минимальная температура
     * @return записи с температурой выше указанной
     */
    @GetMapping("/device/{deviceId}/high-temperature")
    public ResponseEntity<List<TempAndHumidityDataDto>> getHighTemperatureData(
            @PathVariable @NotNull Long deviceId,
            @RequestParam Float minTemperature) {
        log.debug("GET /api/v1/temp-humidity-data/device/{}/high-temperature - получение данных с температурой выше {}", 
                deviceId, minTemperature);
        
        List<TempAndHumidityDataDto> dataList = dataService.getHighTemperatureData(deviceId, minTemperature);
        return ResponseEntity.ok(dataList);
    }

    /**
     * Обновить запись данных
     * @param id идентификатор записи
     * @param dataDto новые данные
     * @return обновленная запись
     */
    @PutMapping("/{id}")
    public ResponseEntity<TempAndHumidityDataDto> updateData(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody TempAndHumidityDataDto dataDto) {
        log.info("PUT /api/v1/temp-humidity-data/{} - обновление записи данных", id);
        
        TempAndHumidityDataDto updatedData = dataService.updateData(id, dataDto);
        return ResponseEntity.ok(updatedData);
    }

    /**
     * Удалить запись данных
     * @param id идентификатор записи
     * @return статус операции
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteData(
            @PathVariable @NotNull Long id) {
        log.info("DELETE /api/v1/temp-humidity-data/{} - удаление записи данных", id);
        
        dataService.deleteData(id);
        return ResponseEntity.noContent().build();
    }
} 