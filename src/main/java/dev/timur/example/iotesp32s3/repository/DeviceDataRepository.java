package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceDataRepository extends JpaRepository<DeviceData,Long> {
    DeviceData getBitDeviceDataById(Long id);
    List<DeviceData> findAll();
}
