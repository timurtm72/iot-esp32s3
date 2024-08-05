package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.DeviceDataDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.mapper.DeviceDataMapper;
import dev.timur.example.iotesp32s3.model.DeviceData;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.repository.DeviceDataRepository;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.service.DeviceDataService;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceDataServiceImpl implements DeviceDataService {
    private final DeviceDataRepository deviceDataRepository;
    private final DeviceDataMapper deviceDataMapper;
    private final MapperUtil mapperUtil;
    private final DeviceRepository deviceRepository;
    @Autowired
    public DeviceDataServiceImpl(DeviceDataRepository deviceDataRepository, DeviceDataMapper deviceDataMapper, MapperUtil mapperUtil, DeviceRepository deviceRepository) {
        this.deviceDataRepository = deviceDataRepository;
        this.deviceDataMapper = deviceDataMapper;
        this.mapperUtil = mapperUtil;
        this.deviceRepository = deviceRepository;
    }
    @Transactional
    @Override
    public DeviceDataDto getById(Long id) {
        DeviceData bitDeviceData =  deviceDataRepository.getBitDeviceDataById(id);
        return bitDeviceData != null ? deviceDataMapper.toDto(bitDeviceData) : null;
    }
    @Transactional
    @Override
    public List<DeviceDataDto> getAll() {
        List<DeviceData> bitDeviceData = deviceDataRepository.findAll();
        if (bitDeviceData == null || bitDeviceData.size() == 0) {
            return null;
        }
        return mapperUtil.mapList(bitDeviceData, DeviceDataDto.class);
    }
    @Transactional
    @Override
    public Status create(DeviceDataDto deviceDataDto, Long deviceId) {
        if(deviceDataDto == null){
            return Status.IS_EMPTY;
        }
        DeviceData bitDeviceData = deviceDataMapper.toEntity(deviceDataDto);
        Device device  = deviceRepository.findDevicesById(deviceId);
        if(device == null){
            return Status.IS_NOT_FOUND;
        }
        bitDeviceData.setDevice(device);
        deviceDataRepository.save(bitDeviceData);
        return Status.IS_OK;
    }
    @Transactional
    @Override
    public Status update(DeviceDataDto deviceDataDto, Long id) {
        return Status.IS_OK;
    }
    @Transactional
    @Override
    public Status delete(Long id) {
        DeviceData bitDeviceDataRemove = deviceDataRepository.getBitDeviceDataById(id);
        if(bitDeviceDataRemove == null){
            return Status.IS_NULL;
        }
        deviceDataRepository.deleteById(id);
        return Status.IS_OK;
    }
}
