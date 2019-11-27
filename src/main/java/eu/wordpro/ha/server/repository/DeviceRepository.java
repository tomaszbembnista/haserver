package eu.wordpro.ha.server.repository;

import eu.wordpro.ha.server.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {

}
