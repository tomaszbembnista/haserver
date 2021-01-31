package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.server.service.SystemService;
import eu.wordpro.ha.server.service.mqtt.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    public SignalProcessorInstancesManager signalProcessorInstancesManager;

    @Autowired
    public MqttClient mqttClient;


    @Override
    public void restart() {
        signalProcessorInstancesManager.clear();
        mqttClient.restart();
    }
}
