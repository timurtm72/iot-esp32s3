package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.DeviceDataDto;
import dev.timur.example.iotesp32s3.model.DeviceData;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceDataMapper implements IMapper<DeviceData, DeviceDataDto>{
    private final MapperUtil mapperUtil;

    @Autowired
    public DeviceDataMapper(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }
    @Override
    public DeviceDataDto toDto(DeviceData deviceData) {
        return mapperUtil.getMapper().map(deviceData, DeviceDataDto.class);
    }

    @Override
    public DeviceData toEntity(DeviceDataDto deviceDataDto) {
        return mapperUtil.getMapper().map(deviceDataDto, DeviceData.class);
    }
}
