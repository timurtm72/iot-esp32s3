package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.BitDeviceDataDto;
import dev.timur.example.iotesp32s3.dto.DeviceDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.mapper.BitDeviceMapper;
import dev.timur.example.iotesp32s3.model.BitDeviceData;
import dev.timur.example.iotesp32s3.model.Device;
import dev.timur.example.iotesp32s3.repository.BitDeviceDataRepository;
import dev.timur.example.iotesp32s3.repository.DeviceRepository;
import dev.timur.example.iotesp32s3.service.BitDeviceDataService;
import dev.timur.example.iotesp32s3.utils.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BitDeviceDataServiceImpl implements BitDeviceDataService {
    private final BitDeviceDataRepository bitDeviceDataRepository;
    private final BitDeviceMapper bitDeviceMapper;
    private final MapperUtil mapperUtil;
    private final DeviceRepository deviceRepository;
    @Autowired
    public BitDeviceDataServiceImpl(BitDeviceDataRepository bitDeviceDataRepository, BitDeviceMapper bitDeviceMapper, MapperUtil mapperUtil, DeviceRepository deviceRepository) {
        this.bitDeviceDataRepository = bitDeviceDataRepository;
        this.bitDeviceMapper = bitDeviceMapper;
        this.mapperUtil = mapperUtil;
        this.deviceRepository = deviceRepository;
    }
    @Transactional
    @Override
    public BitDeviceDataDto getById(Long id) {
        BitDeviceData bitDeviceData =  bitDeviceDataRepository.getBitDeviceDataById(id);
        return bitDeviceData != null ? bitDeviceMapper.toDto(bitDeviceData) : null;
    }
    @Transactional
    @Override
    public List<BitDeviceDataDto> getAll() {
        List<BitDeviceData> bitDeviceData = bitDeviceDataRepository.findAll();
        if (bitDeviceData == null || bitDeviceData.size() == 0) {
            return null;
        }
        return mapperUtil.mapList(bitDeviceData, BitDeviceDataDto.class);
    }
    @Transactional
    @Override
    public Status create(BitDeviceDataDto bitDeviceDataDto,Long deviceId) {
        if(bitDeviceDataDto == null){
            return Status.IS_EMPTY;
        }
        BitDeviceData bitDeviceData = bitDeviceMapper.toEntity(bitDeviceDataDto);
        Device device  = deviceRepository.findDevicesById(deviceId);
        if(device == null){
            return Status.IS_NOT_FOUND;
        }
        bitDeviceData.setDevice(device);
        bitDeviceDataRepository.save(bitDeviceData);
        return Status.IS_OK;
    }
    @Transactional
    @Override
    public Status update(BitDeviceDataDto bitDeviceDataDto, Long id) {
        return Status.IS_OK;
    }
    @Transactional
    @Override
    public Status delete(Long id) {
        BitDeviceData bitDeviceDataRemove = bitDeviceDataRepository.getBitDeviceDataById(id);
        if(bitDeviceDataRemove == null){
            return Status.IS_NULL;
        }
        bitDeviceDataRepository.deleteById(id);
        return Status.IS_OK;
    }
}
