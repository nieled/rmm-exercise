package net.nieled.rmmexercise.repository;

import net.nieled.rmmexercise.domain.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring CRUD repository for {@link Device}.
 */
@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {

    List<Device> findAll();
    List<Device> findByUserEmail(String email);

}
