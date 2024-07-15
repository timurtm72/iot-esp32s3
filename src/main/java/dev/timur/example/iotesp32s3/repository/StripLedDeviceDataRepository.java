package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.StripLedDeviceData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StripLedDeviceDataRepository extends JpaRepository<StripLedDeviceData,Long> {
    StripLedDeviceData getStripLedDataById(Long id);
    List<StripLedDeviceData> findAll();
}
