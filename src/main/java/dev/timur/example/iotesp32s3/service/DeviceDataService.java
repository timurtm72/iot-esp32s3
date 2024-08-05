package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.DeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;

import java.util.List;

public interface DeviceDataService {
    DeviceDataDto getById(Long id);
    List<DeviceDataDto> getAll();

    Status create(DeviceDataDto deviceDataDto, Long deviceId);
    Status update(DeviceDataDto deviceDataDto, Long id);
    Status delete(Long id);
}
