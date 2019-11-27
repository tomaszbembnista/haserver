package eu.wordpro.ha.server.service.mapper;

import eu.wordpro.ha.server.domain.Device;
import eu.wordpro.ha.server.service.dto.DeviceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SpaceMapper.class})
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {

    @Mapping(source = "space.id", target = "spaceId")
    DeviceDTO toDto(Device device);

    @Mapping(source = "spaceId", target = "space")
    @Mapping(target = "processingChains", ignore = true)
    Device toEntity(DeviceDTO deviceDTO);

    default Device fromId(Long id) {
        if (id == null) {
            return null;
        }
        Device result = new Device();
        result.setId(id);
        return result;
    }

}
