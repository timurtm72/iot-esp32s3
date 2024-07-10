package dev.timur.example.iotesp32s3.service;

import dev.timur.example.iotesp32s3.model.Device;

import java.util.List;

public interface DeviseService {
    Device getById(Long id);
    List<Device> getAll();

    boolean create(Device device);
    boolean update(Device device,Long id);
    boolean delete(Long id);

}
