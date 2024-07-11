package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.model.Device;

import java.util.List;

public interface DeviseService {
    DeviceDto getById(Long id);
    List<DeviceDto> getAll();

    boolean create(DeviceDto deviceDto);
    boolean update(DeviceDto deviceDto,Long id);
    boolean delete(Long id);

}
