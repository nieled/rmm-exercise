package net.nieled.rmmexercise.service;

import net.nieled.rmmexercise.domain.Device;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DeviceService {

    /**
     * Saves a device.
     *
     * @param device the entity to save.
     * @return the persisted entity.
     */
    Device save (Device device);

    /**
     * Updates a device.
     *
     * @param device the entity to update.
     * @return the persisted entity.
     */
    Device update (Device device);

    /**
     * Partially updates a device.
     *
     * @param device the entity to partially update.
     * @return the persisted entity.
     */
    Optional<Device> partialUpdate (Device device);

    /**
     * Get all Devices.
     *
     * @return the list of devices.
     */
    List<Device> findAll();

    /**
     * Get the "id" device.
     *
     * @param id the id of the device.
     * @return the device.
     */
    Optional<Device> findOne(Long id);

    /**
     * Delete the "id" device.
     *
     * @param id the id of the device.
     */
    void delete(Long id);

}
