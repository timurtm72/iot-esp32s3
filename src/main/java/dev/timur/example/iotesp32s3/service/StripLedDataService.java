package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.model.StripLedData;
import java.util.List;

public interface StripLedDataService {
    StripLedData getById(Long id);
    List<StripLedData> getAll();

    Status create(StripLedData stripLedData);
    Status update(StripLedData stripLedData,Long id);
    Status delete(Long id);
}

