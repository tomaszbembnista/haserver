package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.server.domain.SignalProcessor;
import eu.wordpro.ha.server.repository.SignalProcessorRepository;
import eu.wordpro.ha.server.service.SignalProcessorService;
import eu.wordpro.ha.server.service.dto.SignalProcessorDTO;
import eu.wordpro.ha.server.service.dto.SpaceDTO;
import eu.wordpro.ha.server.service.mapper.SignalProcessorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SignalProcessorServiceImpl implements SignalProcessorService {

    @Autowired
    SignalProcessorRepository signalProcessorRepository;

    @Autowired
    SignalProcessorMapper signalProcessorMapper;

    @Override
    public SignalProcessorDTO save(SignalProcessorDTO signalProcessorDTO) {
        SignalProcessor signalProcessor = signalProcessorMapper.toEntity(signalProcessorDTO);
        signalProcessor = signalProcessorRepository.save(signalProcessor);
        return signalProcessorMapper.toDto(signalProcessor);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SignalProcessorDTO> findOne(Long signalProcessorId) {
        return signalProcessorRepository.findById(signalProcessorId).map(signalProcessorMapper::toDto);
    }

    @Override
    public List<SignalProcessorDTO> findAll() {
        return signalProcessorRepository.findAll()
                .stream()
                .map(signalProcessorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        signalProcessorRepository.deleteById(id);
    }

}
