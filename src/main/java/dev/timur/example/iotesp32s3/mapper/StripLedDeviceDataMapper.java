package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.StripLedDeviceDataDto;
import dev.timur.example.iotesp32s3.model.StripLedDeviceData;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StripLedDeviceDataMapper implements IMapper<StripLedDeviceData, StripLedDeviceDataDto>{
    private final MapperUtil mapperUtil;

    @Autowired
    public StripLedDeviceDataMapper(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public StripLedDeviceDataDto toDto(StripLedDeviceData stripLedData) {
        return mapperUtil.getMapper().map(stripLedData, StripLedDeviceDataDto.class);
    }

    @Override
    public StripLedDeviceData toEntity(StripLedDeviceDataDto stripLedDataDto) {
        return mapperUtil.getMapper().map(stripLedDataDto, StripLedDeviceData.class);
    }
}

