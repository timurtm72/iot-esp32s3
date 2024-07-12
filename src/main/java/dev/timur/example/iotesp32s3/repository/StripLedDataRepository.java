package dev.timur.example.iotesp32s3.repository;

import dev.timur.example.iotesp32s3.model.StripLedData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StripLedDataRepository extends JpaRepository<StripLedData,Long> {
    StripLedData findStripLedDataById(Long id);
    List<StripLedData> findAll();
}
