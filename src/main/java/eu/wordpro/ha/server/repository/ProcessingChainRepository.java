package eu.wordpro.ha.server.repository;

import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessingChainRepository extends JpaRepository<ProcessingChain, Long> {

    List<ProcessingChain> findAllByInputDeviceId(Long deviceId);
    List<ProcessingChain> findAllByNextId(long nextStepId);

}
