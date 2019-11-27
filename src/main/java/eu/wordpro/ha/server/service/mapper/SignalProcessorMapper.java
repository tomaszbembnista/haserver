package eu.wordpro.ha.server.service.mapper;

import eu.wordpro.ha.server.domain.SignalProcessor;
import eu.wordpro.ha.server.domain.Space;
import eu.wordpro.ha.server.service.dto.SignalProcessorDTO;
import eu.wordpro.ha.server.service.dto.SpaceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SignalProcessorMapper extends EntityMapper<SignalProcessorDTO, SignalProcessor> {

   default SignalProcessor fromId(Long id) {
        if (id == null) {
            return null;
        }
        SignalProcessor result = new SignalProcessor();
        result.setId(id);
        return result;
    }

}
