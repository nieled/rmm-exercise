package net.nieled.rmmexercise.controller;

import lombok.extern.slf4j.Slf4j;
import net.nieled.rmmexercise.domain.Device;
import net.nieled.rmmexercise.domain.Service;
import net.nieled.rmmexercise.repository.DeviceRepository;
import net.nieled.rmmexercise.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class DeviceResource {

    private final DeviceService deviceService;
    private final DeviceRepository deviceRepository;

    public DeviceResource(DeviceService deviceService, DeviceRepository deviceRepository) {
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
    }

    /**
     * {@code GET  /devices} : get all the devices.
     *
     * @return the {@link ResponseEntity} of all the user's devices
     */
    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getAllDevices() {
        log.debug("REST request to get all devices");
        var devices = deviceService.findAll();
        return ResponseEntity.ok().body(devices);
    }

    /**
     * {@code GET  /devices/:id} : get the "id" device.
     *
     * @param id the id of the device.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the device, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devices/{id}")
    public ResponseEntity<Device> getDevice(@PathVariable Long id) {
        log.debug("REST request to get device with id : {}", id);
        var response = deviceService.findOne(id);
        return response
                .map(device -> ResponseEntity.ok().body(device))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * {@code POST  /devices} : Create a new device.
     *
     * @param device the device to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new device. Or the corresponding error.
     * @throws URISyntaxException Incorrect URI syntax.
     */
    @PostMapping("/devices")
    public ResponseEntity<Device> createDevice(@Valid @RequestBody Device device) throws URISyntaxException {
        log.debug("REST request to create a new device : {}", device);
        if (device.getId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New devices cannot already have an ID");
        var result = deviceService.save(device);
        return ResponseEntity
                .created(new URI("/api/devices/" + result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /devices/:id} : Update an existing device.
     *
     * @param id     the id of the existing device to update.
     * @param device the device to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated device. Or the corresponding error.
     */
    @PutMapping("/devices/{id}")
    public ResponseEntity<Device> updateDevice(
            @PathVariable(value = "id", required = false) final long id,
            @Valid @RequestBody Device device
    ) {
        log.debug("REST request to update a device : {}, {}", id, device);
        if (device.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID. ID cannot be null.");
        if (!Objects.equals(id, device.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID. IDs do not match.");
        if (!deviceRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID. Could not find a device with the given ID.");

        var result = deviceService.update(device);
        return ResponseEntity
                .ok()
                .body(result);
    }

    /**
     * {@code PATCH  /devices/:id} : Partial updates an existing device. It ignores null fields.
     *
     * @param id     the id of the existing device to partially update.
     * @param device the device to partially update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partially updated device. Or the corresponding error.
     */
    @PatchMapping("/devices/{id}")
    public ResponseEntity<Device> partialUpdateDevice(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Device device
    ) {
        log.debug("REST request to partially update a device : {}, {}", id, device);
        if (device.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID. ID cannot be null.");
        if (!Objects.equals(id, device.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID. IDs do not match.");
        if (!deviceRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID. Could not find a device with the given ID.");

        var result = deviceService.partialUpdate(device);
        return result
                .map(response -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * {@code DELETE  /devices/:id} : delete the "id" device.
     *
     * @param id the id of the device to delete
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        log.debug("REST request to delete device with id : {}", id);
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/devices/cost")
    public ResponseEntity<BigDecimal> calculateCost() {
        log.debug("REST request to calculate the monthly cost of devices.");
        var devices = deviceService.findAllOwned();
        var cost = devices.stream()
                .map(Device::getServices)
                .flatMap(Set::stream)
                .map(Service::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ResponseEntity.ok().body(cost);
    }
}
