package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.StripLedDeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;

import java.util.List;

public interface StripLedDeviceDataService {
    StripLedDeviceDataDto getById(Long id);
    List<StripLedDeviceDataDto> getAll();

    Status create(StripLedDeviceDataDto stripLedDataDto, Long deviceId);
    Status update(StripLedDeviceDataDto stripLedDataDto, Long id);
    Status delete(Long id);
}

