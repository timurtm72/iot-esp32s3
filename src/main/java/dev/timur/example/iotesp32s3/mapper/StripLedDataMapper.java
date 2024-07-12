package dev.timur.example.iotesp32s3.mapper;

import dev.timur.example.iotesp32s3.dto.StripLedDataDto;
import dev.timur.example.iotesp32s3.model.StripLedData;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StripLedDataMapper  implements IMapper<StripLedData, StripLedDataDto>{
    private final MapperUtil mapperUtil;

    @Autowired
    public StripLedDataMapper(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public StripLedDataDto toDto(StripLedData stripLedData) {
        return mapperUtil.getMapper().map(stripLedData, StripLedDataDto.class);
    }

    @Override
    public StripLedData toEntity(StripLedDataDto stripLedDataDto) {
        return mapperUtil.getMapper().map(stripLedDataDto, StripLedData.class);
    }
}

