package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.serviceimpl.DeviceServiceImpl;
import dev.timur.example.iotesp32s3.utils.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {
    private final DeviceServiceImpl deviceService;
    @Autowired
    public DeviceController(DeviceServiceImpl deviceService) {
        this.deviceService = deviceService;
    }
    @GetMapping()
    public ResponseEntity<List<DeviceDto>> getDevices(){
        List<DeviceDto> devicesDto = deviceService.getAll();
        if(devicesDto == null || devicesDto.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Список устройств пуст");
        }
        return new ResponseEntity<>(devicesDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDevice(@PathVariable("id") Long id){
        DeviceDto deviceDto = deviceService.getById(id);
        if(deviceDto == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Устройство с " + id + " не найдено");
        }
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Response> createDevice(@Valid @RequestBody DeviceDto deviceDto) {
        if (deviceService.create(deviceDto) == Status.IS_EMPTY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ошибка в создании устройства. Введите правильные данные.");
        }
        return ResponseEntity.accepted().body(new Response("Создание устройства прошло успешно", LocalDateTime.now()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateDevice(@PathVariable("id") Long id,
                                                 @Valid @RequestBody DeviceDto deviceDto){
        if(deviceService.update(deviceDto,id) == Status.IS_EMPTY){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Введите правильные данные");
        }
        if(deviceService.update(deviceDto,id) == Status.IS_NOT_FOUND){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Устройство с идентификатором " + id + " не найдено");
        }
        return ResponseEntity.accepted().body(new Response("Обновление устройства прошло успешно", LocalDateTime.now()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteDevice(@PathVariable("id") Long id) {
        if(deviceService.delete(id) == Status.IS_NULL){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Устройство с идентификатором " + id + " не найдено для удаления");
        }
        return ResponseEntity.accepted().body(new Response("Удаление устройства прошло успешно", LocalDateTime.now()));
    }
}
