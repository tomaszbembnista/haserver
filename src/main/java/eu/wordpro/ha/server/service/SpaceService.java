package eu.wordpro.ha.server.service;

import eu.wordpro.ha.server.service.dto.SpaceDTO;

import java.util.List;


public interface SpaceService extends Service<SpaceDTO> {

    List<SpaceDTO> findSpacesIn(Long spaceId);

}
