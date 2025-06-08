package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.service.DeviceService;
import dev.timur.example.iotesp32s3.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin
public class DeviceController {
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<DeviceDto>> getDeviceById(@PathVariable Long id) {
        DeviceDto deviceDto = deviceService.getById(id);
        if (deviceDto != null) {
            return ResponseEntity.ok(new Response<>(deviceDto, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<Response<List<DeviceDto>>> getAllDevices() {
        List<DeviceDto> devices = deviceService.getAll();
        if (devices != null && !devices.isEmpty()) {
            return ResponseEntity.ok(new Response<>(devices, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Response<String>> createDevice(@RequestBody DeviceDto deviceDto) {
        Status status = deviceService.create(deviceDto);
        if (status == Status.IS_OK) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response<>("Устройство создано успешно", status));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response<>("Ошибка создания устройства", status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<String>> updateDevice(@RequestBody DeviceDto deviceDto, @PathVariable Long id) {
        Status status = deviceService.update(deviceDto, id);
        if (status == Status.IS_OK) {
            return ResponseEntity.ok(new Response<>("Устройство обновлено успешно", status));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response<>("Ошибка обновления устройства", status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteDevice(@PathVariable Long id) {
        Status status = deviceService.delete(id);
        if (status == Status.IS_OK) {
            return ResponseEntity.ok(new Response<>("Устройство удалено успешно", status));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>("Устройство не найдено", status));
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<Response<List<DeviceDto>>> getDevicesByName(@PathVariable String name) {
        List<DeviceDto> devices = deviceService.findByNameContaining(name);
        if (devices != null && !devices.isEmpty()) {
            return ResponseEntity.ok(new Response<>(devices, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @GetMapping("/search/location/{location}")
    public ResponseEntity<Response<List<DeviceDto>>> getDevicesByLocation(@PathVariable String location) {
        List<DeviceDto> devices = deviceService.findByLocationContaining(location);
        if (devices != null && !devices.isEmpty()) {
            return ResponseEntity.ok(new Response<>(devices, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }
} 