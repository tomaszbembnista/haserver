package eu.wordpro.ha.server.repository;

import eu.wordpro.ha.server.domain.SignalProcessor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface SignalProcessorRepository extends JpaRepository<SignalProcessor, Long> {

    List<SignalProcessor> findAllBySpaceId(Long id);
}
