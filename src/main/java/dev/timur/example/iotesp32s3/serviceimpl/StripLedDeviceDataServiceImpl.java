package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.StripLedDeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.mapper.StripLedDeviceDataMapper;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.model.StripLedDeviceData;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.repository.StripLedDeviceDataRepository;
import dev.timur.example.iotesp32s3.service.StripLedDeviceDataService;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StripLedDeviceDataServiceImpl implements StripLedDeviceDataService {
    private final StripLedDeviceDataRepository stripLedDataRepository;
    private final StripLedDeviceDataMapper stripLedDataMapper;
    private final MapperUtil mapperUtil;
    private final DeviceRepository deviceRepository;

    @Autowired
    public StripLedDeviceDataServiceImpl(StripLedDeviceDataRepository stripLedDataRepository, StripLedDeviceDataMapper stripLedDataMapper, MapperUtil mapperUtil, DeviceRepository deviceRepository) {
        this.stripLedDataRepository = stripLedDataRepository;
        this.stripLedDataMapper = stripLedDataMapper;
        this.mapperUtil = mapperUtil;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public StripLedDeviceDataDto getById(Long id) {
        StripLedDeviceData stripLedData = stripLedDataRepository.getStripLedDataById(id);
        return stripLedData != null ? stripLedDataMapper.toDto(stripLedData) : null;
    }

    @Override
    public List<StripLedDeviceDataDto> getAll() {
        List<StripLedDeviceData> stripLedData = stripLedDataRepository.findAll();
        if (stripLedData == null || stripLedData.size() == 0) {
            return null;
        }
        return mapperUtil.mapList(stripLedData, StripLedDeviceDataDto.class);
    }

    @Override
    public Status create(StripLedDeviceDataDto stripLedDataDto, Long deviceId) {
        if(stripLedDataDto == null){
            return Status.IS_EMPTY;
        }
        StripLedDeviceData stripLedData = stripLedDataMapper.toEntity(stripLedDataDto);
        Device device  = deviceRepository.findDevicesById(deviceId);
        if(device == null){
            return Status.IS_NOT_FOUND;
        }
        stripLedData.setDevice(device);
       stripLedDataRepository.save(stripLedData);
        return Status.IS_OK;
    }

    @Override
    public Status update(StripLedDeviceDataDto stripLedDataDto, Long id) {
        return Status.IS_OK;
    }

    @Override
    public Status delete(Long id) {
       StripLedDeviceData stripLedDataRemove = stripLedDataRepository.getStripLedDataById(id);
        if(stripLedDataRemove == null){
            return Status.IS_NULL;
        }
        stripLedDataRepository.deleteById(id);
        return Status.IS_OK;
    }
}
