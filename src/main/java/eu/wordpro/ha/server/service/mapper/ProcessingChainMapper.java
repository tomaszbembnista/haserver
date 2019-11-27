package eu.wordpro.ha.server.service.mapper;

import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.service.dto.ProcessingChainDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DeviceMapper.class, SignalProcessorMapper.class} )
public interface ProcessingChainMapper extends EntityMapper<ProcessingChainDTO, ProcessingChain> {

    @Mapping(source = "inputDevice.id", target = "inputDeviceId")
    @Mapping(source = "outputDevice.id", target = "outputDeviceId")
    @Mapping(source = "signalProcessor.id", target = "signalProcessorId")
    @Mapping(source = "next.id", target = "nextId")
    @Override
    ProcessingChainDTO toDto(ProcessingChain processingChain);

    @Mapping(source = "signalProcessorId", target = "signalProcessor")
    @Mapping(source = "inputDeviceId", target = "inputDevice")
    @Mapping(source = "outputDeviceId", target = "outputDevice")
    @Mapping(source = "nextId", target = "next")
    @Override
    ProcessingChain toEntity(ProcessingChainDTO processingChainDTO);

    default ProcessingChain fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcessingChain result = new ProcessingChain();
        result.setId(id);
        return result;
    }


}
