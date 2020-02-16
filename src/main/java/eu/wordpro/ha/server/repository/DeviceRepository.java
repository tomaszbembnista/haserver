package eu.wordpro.ha.server.repository;

import eu.wordpro.ha.server.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findAllBySpaceId(Long id);
}
