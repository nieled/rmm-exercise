package net.nieled.rmmexercise.service.impl;

import net.nieled.rmmexercise.domain.Service;
import net.nieled.rmmexercise.repository.ServiceRepository;
import net.nieled.rmmexercise.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final Logger logger = LoggerFactory.getLogger(ServiceServiceImpl.class);

    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Service save(Service service) {
        logger.debug("Request to save Service : {}", service);
        return serviceRepository.save(service);
    }

    @Override
    public void delete(Long id) {
        logger.debug("Request to delete Service with id : {}", id);
        serviceRepository.deleteById(id);
    }
}
