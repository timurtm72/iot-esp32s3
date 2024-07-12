package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.BitDeviceDataDto;
import dev.timur.example.iotesp32s3.model.BitDeviceData;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BitDeviceMapper  implements IMapper<BitDeviceData, BitDeviceDataDto>{
    private final MapperUtil mapperUtil;

    @Autowired
    public BitDeviceMapper(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }
    @Override
    public BitDeviceDataDto toDto(BitDeviceData bitDeviceData) {
        return mapperUtil.getMapper().map(bitDeviceData, BitDeviceDataDto.class);
    }

    @Override
    public BitDeviceData toEntity(BitDeviceDataDto bitDeviceDataDto) {
        return mapperUtil.getMapper().map(bitDeviceDataDto, BitDeviceData.class);
    }
}
