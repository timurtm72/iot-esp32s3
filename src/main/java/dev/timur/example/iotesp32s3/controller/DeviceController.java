package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.model.Device;
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
    public ResponseEntity<List<Device>> getDevices(){
        List<Device> devices = deviceService.getAll();
        if(devices == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Список устройств пуст");
        }
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDevice(@PathVariable("id") Long id){
        Device device = deviceService.getById(id);
        if(device == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Устройство с " + id + " не найдено");
        }
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Response> createDevice(@Valid @RequestBody Device device) {
        if (deviceService.create(device)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ошибка в создании устройства");
        }
        return ResponseEntity.accepted().body(new Response("Создание устройства прошло успешно", LocalDateTime.now()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateDevice(@PathVariable("id") Long id,
                                                 @Valid @RequestBody Device device){
        if(!deviceService.update(device,id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Устройство с идентификатором " + id + " не найдено");
        }
        return ResponseEntity.accepted().body(new Response("Обновление устройства прошло успешно", LocalDateTime.now()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteDevice(@PathVariable("id") Long id) {
        if(!deviceService.delete(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Неверно заданный id для удаления");
        }
        return ResponseEntity.accepted().body(new Response("Удаление устройства прошло успешно", LocalDateTime.now()));
    }
}
