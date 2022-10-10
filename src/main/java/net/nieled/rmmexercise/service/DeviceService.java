package net.nieled.rmmexercise.service;

import net.nieled.rmmexercise.domain.Device;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeviceService {

    /**
     * Saves a device
     *
     * @param device the entity to save.
     * @return the persisted entity.
     */
    Device save (Device device);

    /**
     * Updates a device
     *
     * @param device the entity to update.
     * @return the persisted entity.
     */
    Device update (Device device);

    /**
     * Get all Devices
     *
     * @return the list of devices.
     */
    List<Device> findAll();

    /**
     * Delete the "id" device.
     *
     * @param id the id of the device.
     */
    void delete(Long id);
}
