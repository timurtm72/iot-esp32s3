package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    Optional<Device> findDevicesById(Long id);
    List<Device> findAll();
}
