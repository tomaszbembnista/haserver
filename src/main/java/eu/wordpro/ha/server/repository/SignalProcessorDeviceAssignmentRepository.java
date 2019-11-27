package eu.wordpro.ha.server.repository;

import eu.wordpro.ha.server.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignalProcessorDeviceAssignmentRepository extends JpaRepository<Space, Long> {
}
