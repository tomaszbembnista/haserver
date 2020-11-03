package eu.wordpro.ha.server.service.mapper;

import eu.wordpro.ha.server.domain.Space;
import eu.wordpro.ha.server.service.dto.SpaceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpaceMapper extends EntityMapper<SpaceDTO, Space> {

    @Mapping(source = "parent.id", target = "parentId")
    SpaceDTO toDto(Space space);

    @Mapping(source = "parentId", target = "parent")
    @Mapping(target = "subspaces", ignore = true)
    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "signalProcessors", ignore = true)
    Space toEntity(SpaceDTO spaceDTO);

    default Space fromId(Long id) {
        if (id == null) {
            return null;
        }
        Space space = new Space();
        space.setId(id);
        return space;
    }

}
