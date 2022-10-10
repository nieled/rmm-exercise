package net.nieled.rmmexercise.service;


import net.nieled.rmmexercise.domain.Service;

@org.springframework.stereotype.Service
public interface ServiceService {

    /**
     * Saves a service.
     *
     * @param service the service to save
     * @return the peristed entity
     */
    Service save(Service service);

    /**
     * Delete the "id" service.
     *
     * @param id the id of the service
     */
    void delete(Long id);
}
