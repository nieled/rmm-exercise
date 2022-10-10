package net.nieled.rmmexercise.controller;

import lombok.extern.slf4j.Slf4j;
import net.nieled.rmmexercise.domain.Service;
import net.nieled.rmmexercise.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api")
public class ServiceResource {

    private final ServiceService serviceService;

    public ServiceResource(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * {@code POST  /services} : Create a new service.
     *
     * @param service the service to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new service. Or the corresponding error.
     * @throws URISyntaxException Incorrect URI syntax.
     */
    @PostMapping("/services")
    public ResponseEntity<Service> createService(@RequestBody Service service) throws URISyntaxException {
        log.debug("REST request to create a new service : {}", service);
        if (service.getId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New services cannot already have an ID");
        var result = serviceService.save(service);
        return ResponseEntity
                .created(new URI("/api/services/" + result.getId()))
                .body(result);
    }

    /**
     * {@code DELETE  /services/:id} : delete the "id" service.
     *
     * @param id the id of the service to delete
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        log.debug("REST request to delete service with id : {}", id);
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
