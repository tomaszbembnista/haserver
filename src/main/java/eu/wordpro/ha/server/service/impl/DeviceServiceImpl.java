package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.api.model.ProcessingData;
import eu.wordpro.ha.server.domain.Device;
import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.domain.Space;
import eu.wordpro.ha.server.repository.DeviceRepository;
import eu.wordpro.ha.server.repository.ProcessingChainRepository;
import eu.wordpro.ha.server.service.DeviceService;
import eu.wordpro.ha.server.service.ProcessingChainService;
import eu.wordpro.ha.server.service.dto.DeviceDTO;
import eu.wordpro.ha.server.service.dto.ProcessingChainDTO;
import eu.wordpro.ha.server.service.mapper.DeviceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceMapper deviceMapper;


    @Autowired
    ProcessingChainService processingChainService;

    @Autowired
    ProcessingChainRepository processingChainRepository;



    Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Override
    public DeviceDTO save(DeviceDTO deviceDTO) {
        Device device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceDTO> findOne(Long id) {
        return deviceRepository.findById(id).map(deviceMapper::toDto);
    }

    @Override
    public List<DeviceDTO> findAll() {
        return deviceRepository.findAll()
                .stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        deviceRepository.deleteById(id);
    }

    @Override
    public String getMqttTopic(Long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null){
            logger.warn("Device with id {} not found", deviceId);
            return null;
        }
        String slug = device.getSlug();
        Space space = device.getSpace();
        while (space != null){
            slug = space.getSlug() + "/" + slug;
            space = space.getParent();
        }
        return slug;
    }

    @Override
    public void processInputData(Long deviceId, ProcessingData input) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null){
            logger.warn("Device with id {} not found", deviceId);
            return;
        }
        Set<ProcessingChain> processingChains = device.getProcessingChains();
        for (ProcessingChain processingChain : processingChains){
            processingChainService.executeProcessingChain(processingChain, input.copy());
        }
    }

    @Override
    public List<DeviceDTO> findDevicesInSpace(Long id) {
        return deviceRepository.findAllBySpaceId(id)
                .stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
    }




}
