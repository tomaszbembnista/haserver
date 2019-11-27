package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.repository.ProcessingChainRepository;
import eu.wordpro.ha.server.service.ProcessingChainService;
import eu.wordpro.ha.server.service.dto.ProcessingChainDTO;
import eu.wordpro.ha.server.service.mapper.ProcessingChainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProcessingChainServiceImpl implements ProcessingChainService {

    @Autowired
    ProcessingChainRepository processingChainRepository;

    @Autowired
    ProcessingChainMapper processingChainMapper;

    @Override
    public ProcessingChainDTO save(ProcessingChainDTO spaceDTO) {
        ProcessingChain processingChain = processingChainMapper.toEntity(spaceDTO);
        processingChain = processingChainRepository.save(processingChain);
        return processingChainMapper.toDto(processingChain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessingChainDTO> findOne(Long spaceId) {
        return processingChainRepository.findById(spaceId).map(processingChainMapper::toDto);
    }

    @Override
    public List<ProcessingChainDTO> findAll() {
        return processingChainRepository.findAll()
                .stream()
                .map(processingChainMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        processingChainRepository.deleteById(id);
    }

}
