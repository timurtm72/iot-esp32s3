package dev.timur.example.iotesp32s3.utils;

import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.model.BitDeviceData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MapperUtil {
    private final ModelMapper mapper;
    @Autowired
    public MapperUtil(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public  <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> mapper.map(element, targetClass))
                .toList();
    }

    public ModelMapper getMapper() {
        return new ModelMapper();
    }
}