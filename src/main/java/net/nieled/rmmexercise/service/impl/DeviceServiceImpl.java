package net.nieled.rmmexercise.service.impl;

import net.nieled.rmmexercise.domain.Device;
import net.nieled.rmmexercise.repository.DeviceRepository;
import net.nieled.rmmexercise.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for {@link Device}
 */
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device save(Device device) {
        logger.debug("Request to save Device : {}", device);
        return deviceRepository.save(device);
    }

    @Override
    public Device update(Device device) {
        logger.debug("Request to update Device : {}", device);
        return deviceRepository.save(device);
    }

    @Override
    public Optional<Device> partialUpdate(Device device) {
        logger.debug("Request to partially update Device : {}", device);
        return deviceRepository
                .findById(device.getId())
                .map(existingDevice -> {
                    if (device.getSystemName() != null) existingDevice.setSystemName(device.getSystemName());
                    if (device.getDeviceType() != null) existingDevice.setDeviceType(device.getDeviceType());
                    return existingDevice;
                })
                .map(deviceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Device> findAll() {
        logger.debug("Request to get all Devices");
        return deviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Device> findOne(Long id) {
        logger.debug("Request to get the Device with id : {}", id);
        return deviceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        logger.debug("Request to delete Device with id : {}", id);
        deviceRepository.deleteById(id);
    }
}
