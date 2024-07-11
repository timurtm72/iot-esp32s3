package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.model.BitDeviceData;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.model.StripLedData;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class DeviceMapper implements IMapper<Device, DeviceDto> {
    private final MapperUtil mapperUtil;

    @Autowired
    public DeviceMapper(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }


    @Override
    public DeviceDto toDto(Device device) {
        DeviceDto deviceDto = mapperUtil.getMapper().map(device, DeviceDto.class);
        deviceDto.setInputValues(
                mapperUtil.mapList(device.getInputValues(), BitDeviceData.class)
        );
        deviceDto.setLedValues(
                mapperUtil.mapList(device.getLedValues(), StripLedData.class)
        );
        return deviceDto;
    }

    @Override
    public Device toEntity(DeviceDto deviceDto) {
        Device device = mapperUtil.getMapper().map(deviceDto, Device.class);
        device.setInputValues(
                mapperUtil.mapList(deviceDto.getInputValues(), BitDeviceData.class)
        );
        device.setLedValues(
                mapperUtil.mapList(deviceDto.getLedValues(), StripLedData.class)
        );
        return device;
    }
}
