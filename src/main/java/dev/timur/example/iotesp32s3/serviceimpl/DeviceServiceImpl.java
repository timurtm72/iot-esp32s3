
package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.mapper.DeviceMapper;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.repository.BitDeviceDataRepository;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.repository.StripLedDeviceDataRepository;
import dev.timur.example.iotesp32s3.service.DeviseService;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DeviceServiceImpl implements DeviseService {
    private final DeviceRepository deviceRepository;
    private final MapperUtil mapperUtil;
    private final DeviceMapper deviceMapper;
    private final StripLedDeviceDataRepository stripLedDataRepository;
    private final BitDeviceDataRepository bitDeviceRepository;
    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, MapperUtil mapperUtil, DeviceMapper deviceMapper, StripLedDeviceDataRepository stripLedDataRepository, BitDeviceDataRepository bitDeviceRepository) {
        this.deviceRepository = deviceRepository;
        this.mapperUtil = mapperUtil;
        this.deviceMapper = deviceMapper;
        this.stripLedDataRepository = stripLedDataRepository;
        this.bitDeviceRepository = bitDeviceRepository;
    }
    @Transactional
    @Override
    public DeviceDto getById(Long id) {
        Device device =  deviceRepository.findDevicesById(id);
        return  device != null ? deviceMapper.toDto(device) : null;
    }
    @Transactional
    @Override
    public List<DeviceDto> getAll() {
        List<Device> devices = deviceRepository.findAll();
        if (devices == null || devices.size() == 0) {
            return null;
        }
        return mapperUtil.mapList(devices, DeviceDto.class);
    }
    @Transactional
    @Override
    public Status create(DeviceDto deviceDto) {
        if (deviceDto == null) {
            return Status.IS_EMPTY;
        }
        Device newDevice = deviceMapper.toEntity(deviceDto);
        newDevice.setInputValues(null);
        newDevice.setLedValues(null);
        deviceRepository.save(newDevice);
        return Status.IS_OK;
    }
    @Transactional
    @Override
    public Status update(DeviceDto deviceDto, Long id) {
        if (deviceDto == null) {
            return Status.IS_EMPTY;
        }
        deviceDto.setId(id);
        Device device = deviceMapper.toEntity(deviceDto);
        Device updateDevice = deviceRepository.findDevicesById(id);
        if (updateDevice == null) {
            return Status.IS_NOT_FOUND;
        }

        if (device.getName() != null && !device.getName().isEmpty()) {
            updateDevice.setName(device.getName());
        }

        if (device.getDescription() != null && !device.getDescription().isEmpty()) {
            updateDevice.setDescription(device.getDescription());
        }
        if(device.getLedValues() != null && !device.getLedValues().isEmpty()){
            updateDevice.setLedValues(device.getLedValues());
        }
        if(device.getInputValues() != null && !device.getInputValues().isEmpty()){
            updateDevice.setInputValues(device.getInputValues());
        }

        deviceRepository.save(updateDevice);
        return Status.IS_OK;
    }
    @Transactional
    @Override
    public Status delete(Long id) {
        Device removeDevice = deviceRepository.findDevicesById(id);
        if(removeDevice == null){
            return Status.IS_NULL;
        }
        deviceRepository.deleteById(id);
        return Status.IS_OK;
    }
}
