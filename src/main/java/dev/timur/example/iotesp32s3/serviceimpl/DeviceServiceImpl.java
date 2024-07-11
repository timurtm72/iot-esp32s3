package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.mapper.DeviceMapper;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.service.DeviseService;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class DeviceServiceImpl implements DeviseService {
    private final DeviceRepository deviceRepository;
    private final MapperUtil mapperUtil;
    private final DeviceMapper deviceMapper;
    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, MapperUtil mapperUtil, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.mapperUtil = mapperUtil;
        this.deviceMapper = deviceMapper;
    }
    @Transactional
    @Override
    public DeviceDto getById(Long id) {
        Device device =  deviceRepository.findDevicesById(id).orElse(null);
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
    public boolean create(DeviceDto deviceDto) {
        if (deviceDto != null) {
            Device newDevice = deviceMapper.toEntity(deviceDto);
            newDevice.setName(deviceDto.getName());
            newDevice.setDescription(deviceDto.getName());
            if(deviceDto.getLedValues() == null) {
                deviceDto.setLedValues(new ArrayList<>());
                newDevice.setLedValues(deviceDto.getLedValues());
            }
            if(deviceDto.getInputValues() == null) {
                deviceDto.setInputValues(new ArrayList<>());
                newDevice.setInputValues(deviceDto.getInputValues());
            }
            deviceRepository.save(newDevice);
            return true;
        }else{
            return false;
        }
    }
    @Transactional
    @Override
    public boolean update(DeviceDto deviceDto, Long id) {
        if(deviceDto == null){
            return false;
        }
        deviceDto.setId(id);
        Device device = deviceMapper.toEntity(deviceDto);
        Device updateDevice = deviceRepository.findDevicesById(id).orElse(null);
        if(updateDevice == null){
            return false;
        }
        updateDevice.setName(device.getName());
        updateDevice.setDescription(device.getDescription());
        updateDevice.setInputValues(device.getInputValues());
        updateDevice.setLedValues(device.getLedValues());
        deviceRepository.save(updateDevice);
       return true;
    }
    @Transactional
    @Override
    public boolean delete(Long id) {
        Device removeDevice = deviceRepository.findDevicesById(id).orElse(null);
        if(removeDevice == null){
            return false;
        }
        deviceRepository.deleteById(id);
        return true;
    }
}
