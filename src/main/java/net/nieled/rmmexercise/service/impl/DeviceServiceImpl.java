package net.nieled.rmmexercise.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nieled.rmmexercise.domain.Device;
import net.nieled.rmmexercise.repository.DeviceRepository;
import net.nieled.rmmexercise.repository.UserRepository;
import net.nieled.rmmexercise.security.SecurityUtils;
import net.nieled.rmmexercise.service.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for {@link Device}
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Override
    public Device save(Device device) {
        log.debug("Request to save Device : {}", device);
        device.setUser(
                SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findByEmail).orElse(null)
        );
        return deviceRepository.save(device);
    }

    @Override
    public Device update(Device device) {
        log.debug("Request to update Device : {}", device);
        return deviceRepository.save(device);
    }

    @Override
    public Optional<Device> partialUpdate(Device device) {
        log.debug("Request to partially update Device : {}", device);
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
        log.debug("Request to get all Devices");
        return SecurityUtils
                .getCurrentUserLogin()
                .map(deviceRepository::findByUserEmail)
                .orElse(Collections.emptyList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Device> findOne(Long id) {
        log.debug("Request to get the Device with id : {}", id);
        return deviceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Device with id : {}", id);
        deviceRepository.deleteById(id);
    }
}
