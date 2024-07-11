package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.enums.Status;

import java.util.List;

public interface DeviseService {
    DeviceDto getById(Long id);
    List<DeviceDto> getAll();

    Status create(DeviceDto deviceDto);
    Status update(DeviceDto deviceDto,Long id);
    Status delete(Long id);

}
