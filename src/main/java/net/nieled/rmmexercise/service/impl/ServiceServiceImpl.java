package net.nieled.rmmexercise.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.nieled.rmmexercise.domain.Service;
import net.nieled.rmmexercise.repository.ServiceRepository;
import net.nieled.rmmexercise.service.ServiceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for {@link Service}
 */
@Slf4j
@org.springframework.stereotype.Service
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Service save(Service service) {
        log.debug("Request to save Service : {}", service);
        return serviceRepository.save(service);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Service with id : {}", id);
        serviceRepository.deleteById(id);
    }
}
