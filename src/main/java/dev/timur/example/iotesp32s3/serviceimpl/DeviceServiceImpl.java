package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.mapper.DeviceMapper;
import dev.timur.example.iotesp32s3.model.BitDeviceData;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.model.StripLedData;
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
    public Status create(DeviceDto deviceDto) {
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
            return Status.IS_OK;
        }else{
            return Status.IS_EMPTY;
        }
    }
    @Transactional
    @Override
    public Status update(DeviceDto deviceDto, Long id) {
        if (deviceDto == null) {
            return Status.IS_EMPTY;
        }
        deviceDto.setId(id);
        Device device = deviceMapper.toEntity(deviceDto);
        Device updateDevice = deviceRepository.findDevicesById(id).orElse(null);
        if (updateDevice == null) {
            return Status.IS_NOT_FOUND;
        }

        // Update name if it is not empty or null
        if (device.getName() != null && !device.getName().isEmpty()) {
            updateDevice.setName(device.getName());
        }

        // Update description if it is not empty or null
        if (device.getDescription() != null && !device.getDescription().isEmpty()) {
            updateDevice.setDescription(device.getDescription());
        }

        // Update inputValues
        List<BitDeviceData> existingInputValues = updateDevice.getInputValues();
        List<BitDeviceData> editingInputValues = device.getInputValues();

        int sizeEx = existingInputValues.size() - 1;
        int sizeEd = editingInputValues.size() - 1;

        if(existingInputValues.get(sizeEx) != device.getInputValues().get(sizeEd)){
            existingInputValues.add(editingInputValues.get(sizeEd));
            updateDevice.setInputValues(existingInputValues);
        }

        // Update ledValues
        List<StripLedData> existingLedValues = updateDevice.getLedValues();
        List<StripLedData> editingLedValues = device.getLedValues();

        sizeEx = existingLedValues.size() - 1;
        sizeEd = editingLedValues.size() - 1;

        if(existingLedValues.get(sizeEx) != device.getLedValues().get(sizeEd)){
            existingLedValues.add(editingLedValues.get(sizeEd));
            updateDevice.setLedValues(existingLedValues);
        }

        deviceRepository.save(updateDevice);
        return Status.IS_OK;
    }

    @Transactional
    @Override
    public Status delete(Long id) {
        Device removeDevice = deviceRepository.findDevicesById(id).orElse(null);
        if(removeDevice == null){
            return Status.IS_NULL;
        }
        deviceRepository.deleteById(id);
        return Status.IS_OK;
    }
}
