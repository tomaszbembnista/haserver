package eu.wordpro.ha.server.repository;

import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingChainRepository extends JpaRepository<ProcessingChain, Long> {


}