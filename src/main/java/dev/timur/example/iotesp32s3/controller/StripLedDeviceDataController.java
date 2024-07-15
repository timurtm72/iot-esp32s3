package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.StripLedDeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.service.StripLedDeviceDataService;
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
@RequestMapping("/led_device")
public class StripLedDeviceDataController {
    private final StripLedDeviceDataService stripLedDeviceDataService;
    @Autowired
    public StripLedDeviceDataController(StripLedDeviceDataService stripLedDeviceDataService) {
        this.stripLedDeviceDataService = stripLedDeviceDataService;
    }

    @GetMapping()
    public ResponseEntity<List<StripLedDeviceDataDto>> getStripLedDeviceData(){
        List<StripLedDeviceDataDto> stripLedDevicesDataDto = stripLedDeviceDataService.getAll();
        if(stripLedDevicesDataDto == null ||stripLedDevicesDataDto.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Список каналов светодиодов устройства пуст");
        }
        return new ResponseEntity<>(stripLedDevicesDataDto, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<StripLedDeviceDataDto> getStripLedDeviceData(@PathVariable("id") Long id){
        StripLedDeviceDataDto stripLedDeviceDataDto = stripLedDeviceDataService.getById(id);
        if(stripLedDeviceDataDto == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Светодиодные каналы устройства отсутствуют");
        }
        return new ResponseEntity<>(stripLedDeviceDataDto, HttpStatus.OK);
    }
    @PostMapping("/add_strip_led_data/{id}")
    public ResponseEntity<Response> createStripLedDeviceData(@Valid @RequestBody StripLedDeviceDataDto stripLedDeviceDataDto, @PathVariable("id") Long id) {
        Status status =stripLedDeviceDataService.create(stripLedDeviceDataDto,id);
        if ( status == Status.IS_EMPTY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ошибка в создании каналов светодиодов устройства. Введите правильные данные.");
        } else if (status == Status.IS_NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не найдено каналы светодиодов по введеному deviceId");

        }
        return ResponseEntity.accepted().body(new Response("Создание каналов светодиодов устройства прошло успешно", LocalDateTime.now()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteStripLedDataDevice(@PathVariable("id") Long id) {
        if(stripLedDeviceDataService.delete(id) == Status.IS_NULL){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Каналы светодиодов устройства с идентификатором " + id + " не найдено для удаления");
        }
        return ResponseEntity.accepted().body(new Response("Удаление каналов светодиодов устройства прошло успешно", LocalDateTime.now()));
    }
}
