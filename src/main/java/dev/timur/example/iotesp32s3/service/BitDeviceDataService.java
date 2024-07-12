package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.model.BitDeviceData;

import java.util.List;

public interface BitDeviceDataService {
    BitDeviceData getById(Long id);
    List<BitDeviceData> getAll();

    Status create(BitDeviceData bitDeviceData);
    Status update(BitDeviceData bitDeviceData,Long id);
    Status delete(Long id);
}
