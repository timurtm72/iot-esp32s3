package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.BitDeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;

import java.util.List;

public interface BitDeviceDataService {
    BitDeviceDataDto getById(Long id);
    List<BitDeviceDataDto> getAll();

    Status create(BitDeviceDataDto bitDeviceDataDto,Long deviceId);
    Status update(BitDeviceDataDto bitDeviceDataDto,Long id);
    Status delete(Long id);
}
