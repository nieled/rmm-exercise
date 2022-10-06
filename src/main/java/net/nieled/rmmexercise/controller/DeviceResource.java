package net.nieled.rmmexercise.controller;

import net.nieled.rmmexercise.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class DeviceResource {

    private final Logger logger = LoggerFactory.getLogger(DeviceResource.class);

    private final DeviceService deviceService;

    public DeviceResource(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
}
