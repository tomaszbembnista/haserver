package eu.wordpro.ha.server.service;

import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.server.service.dto.DeviceDTO;
import eu.wordpro.ha.server.service.dto.ProcessingChainDTO;

import java.util.LinkedList;
import java.util.List;

public interface DeviceService extends Service<DeviceDTO> {
    String getMqttTopic(Long deviceId);
    void processInputData(Long deviceId, LinkedList<SignalProcessorData> inputs);
    List<DeviceDTO> findDevicesInSpace(Long id);
}
