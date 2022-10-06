package net.nieled.rmmexercise.controller;

import net.nieled.rmmexercise.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class ServiceResource {

    private final Logger logger = LoggerFactory.getLogger(ServiceResource.class);

    private final ServiceService serviceService;


    public ServiceResource(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
}
