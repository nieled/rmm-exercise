package net.nieled.rmmexercise.repository;

import net.nieled.rmmexercise.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring CRUD repository for {@link Device}.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByUserEmail(String email);

}
