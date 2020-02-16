package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.server.domain.Space;
import eu.wordpro.ha.server.repository.SpaceRepository;
import eu.wordpro.ha.server.service.SpaceService;
import eu.wordpro.ha.server.service.dto.SpaceDTO;
import eu.wordpro.ha.server.service.mapper.SpaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SpaceServiceImpl implements SpaceService {

    @Autowired
    SpaceRepository spaceRepository;

    @Autowired
    SpaceMapper spaceMapper;

    @Override
    public SpaceDTO save(SpaceDTO spaceDTO) {
        Space space = spaceMapper.toEntity(spaceDTO);
        space = spaceRepository.save(space);
        return spaceMapper.toDto(space);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpaceDTO> findOne(Long spaceId) {
        return spaceRepository.findById(spaceId).map(spaceMapper::toDto);
    }

    @Override
    public List<SpaceDTO> findAll() {
        return spaceRepository.findAll()
                .stream()
                .map(spaceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        //TODO: remove attached subspaces and devices or detach them before removing
        spaceRepository.deleteById(id);
    }

    @Override
    public List<SpaceDTO> findSpacesIn(Long spaceId) {
        return spaceRepository.findAllByParentId(spaceId)
                .stream()
                .map(spaceMapper::toDto)
                .collect(Collectors.toList());
    }
}
