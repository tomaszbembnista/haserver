package eu.wordpro.ha.server.repository;

import eu.wordpro.ha.server.domain.SignalProcessor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignalProcessorRepository extends JpaRepository<SignalProcessor, Long> {
}
