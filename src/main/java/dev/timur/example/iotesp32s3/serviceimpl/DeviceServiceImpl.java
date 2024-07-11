package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.service.DeviseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class DeviceServiceImpl implements DeviseService {
    DeviceRepository deviceRepository;
    @Transactional
    @Override
    public Device getById(Long id) {
        return deviceRepository.findDevicesById(id).orElse(null);
    }
    @Transactional
    @Override
    public List<Device> getAll() {
        List<Device> devices = deviceRepository.findAll();
        if (devices == null || devices.size() == 0) {
            return null;
        }
        return devices;
    }
    @Transactional
    @Override
    public boolean create(Device device) {
        if (device != null) {
            Device newDevice = new Device();
            newDevice.setName(device.getName());
            newDevice.setDescription(device.getName());
            if(device.getLedValues() == null) {
                device.setLedValues(new ArrayList<>());
                newDevice.setLedValues(device.getLedValues());
            }
            if(device.getInputValues() == null) {
                device.setInputValues(new ArrayList<>());
                newDevice.setInputValues(device.getInputValues());
            }
            return true;
        }else{
            return false;
        }
    }
    @Transactional
    @Override
    public boolean update(Device device, Long id) {
        if(device == null){
            return false;
        }
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
