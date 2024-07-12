package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.BitDeviceData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BitDeviceDataRepository extends JpaRepository<BitDeviceData,Long> {
    BitDeviceData getBitDeviceDataById(Long id);
    List<BitDeviceData> findAll();
}
