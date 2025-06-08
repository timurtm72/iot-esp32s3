package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.DeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.service.DeviceDataService;
import dev.timur.example.iotesp32s3.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/device-data")
@CrossOrigin
public class DeviceDataController {
    private final DeviceDataService deviceDataService;

    @Autowired
    public DeviceDataController(DeviceDataService deviceDataService) {
        this.deviceDataService = deviceDataService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<DeviceDataDto>> getDeviceDataById(@PathVariable Long id) {
        DeviceDataDto deviceDataDto = deviceDataService.getById(id);
        if (deviceDataDto != null) {
            return ResponseEntity.ok(new Response<>(deviceDataDto, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<Response<List<DeviceDataDto>>> getAllDeviceData() {
        List<DeviceDataDto> deviceDataList = deviceDataService.getAll();
        if (deviceDataList != null && !deviceDataList.isEmpty()) {
            return ResponseEntity.ok(new Response<>(deviceDataList, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @PostMapping("/device/{deviceId}")
    public ResponseEntity<Response<String>> createDeviceData(@RequestBody DeviceDataDto deviceDataDto, @PathVariable Long deviceId) {
        Status status = deviceDataService.create(deviceDataDto, deviceId);
        if (status == Status.IS_OK) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response<>("Данные устройства созданы успешно", status));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response<>("Ошибка создания данных устройства", status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<String>> updateDeviceData(@RequestBody DeviceDataDto deviceDataDto, @PathVariable Long id) {
        Status status = deviceDataService.update(deviceDataDto, id);
        if (status == Status.IS_OK) {
            return ResponseEntity.ok(new Response<>("Данные устройства обновлены успешно", status));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response<>("Ошибка обновления данных устройства", status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteDeviceData(@PathVariable Long id) {
        Status status = deviceDataService.delete(id);
        if (status == Status.IS_OK) {
            return ResponseEntity.ok(new Response<>("Данные устройства удалены успешно", status));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>("Данные устройства не найдены", status));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<Response<List<DeviceDataDto>>> getDeviceDataByDeviceId(@PathVariable Long deviceId) {
        List<DeviceDataDto> deviceDataList = deviceDataService.findByDeviceId(deviceId);
        if (deviceDataList != null && !deviceDataList.isEmpty()) {
            return ResponseEntity.ok(new Response<>(deviceDataList, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @GetMapping("/device/{deviceId}/latest")
    public ResponseEntity<Response<DeviceDataDto>> getLatestDeviceData(@PathVariable Long deviceId) {
        DeviceDataDto deviceDataDto = deviceDataService.findLatestByDeviceId(deviceId);
        if (deviceDataDto != null) {
            return ResponseEntity.ok(new Response<>(deviceDataDto, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @GetMapping("/period")
    public ResponseEntity<Response<List<DeviceDataDto>>> getDeviceDataByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<DeviceDataDto> deviceDataList = deviceDataService.findByTimestampBetween(start, end);
        if (deviceDataList != null && !deviceDataList.isEmpty()) {
            return ResponseEntity.ok(new Response<>(deviceDataList, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }
} 