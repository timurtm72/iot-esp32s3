package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.BitDeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.service.BitDeviceDataService;
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
@RequestMapping("/bit_device")
public class BitDeviceDataController {
    private final BitDeviceDataService bitDeviceDataService;
    @Autowired
    public BitDeviceDataController(BitDeviceDataService bitDeviceDataService) {
        this.bitDeviceDataService = bitDeviceDataService;
    }

    @GetMapping()
    public ResponseEntity<List<BitDeviceDataDto>> getBitDevicesData(){
        List<BitDeviceDataDto> bitDevicesDataDto = bitDeviceDataService.getAll();
        if(bitDevicesDataDto == null ||bitDevicesDataDto.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Список входа устройств пуст");
        }
        return new ResponseEntity<>(bitDevicesDataDto, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BitDeviceDataDto> getBitDeviceData(@PathVariable("id") Long id){
        BitDeviceDataDto bitDeviceDataDto = bitDeviceDataService.getById(id);
        if(bitDeviceDataDto == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Вход устройства пуст");
        }
        return new ResponseEntity<>(bitDeviceDataDto, HttpStatus.OK);
    }
    @PostMapping("/add_bit_input_data/{id}")
    public ResponseEntity<Response> createBitDeviceData(@Valid @RequestBody BitDeviceDataDto bitDeviceDataDto, @PathVariable("id") Long id) {
        Status status = bitDeviceDataService.create(bitDeviceDataDto,id);
        if ( status == Status.IS_EMPTY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ошибка в создании входа устройства. Введите правильные данные.");
        } else if (status == Status.IS_NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не найдено устройство по введеному deviceId");

        }
        return ResponseEntity.accepted().body(new Response("Создание входа устройства прошло успешно", LocalDateTime.now()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteBitDataDevice(@PathVariable("id") Long id) {
        if(bitDeviceDataService.delete(id) == Status.IS_NULL){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Вход устройства с идентификатором " + id + " не найдено для удаления");
        }
        return ResponseEntity.accepted().body(new Response("Удаление входа устройства прошло успешно", LocalDateTime.now()));
    }
}
