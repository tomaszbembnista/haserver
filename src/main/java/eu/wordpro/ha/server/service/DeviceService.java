package eu.wordpro.ha.server.service;

import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.server.service.dto.DeviceDTO;

import java.util.LinkedList;

public interface DeviceService extends Service<DeviceDTO> {
    String getMqttTopic(Long deviceId);
    void processInputData(Long deviceId, LinkedList<SignalProcessorData> inputs);
}
