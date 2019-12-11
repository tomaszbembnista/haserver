package eu.wordpro.ha.server.service.mqtt;

import eu.wordpro.ha.server.service.DeviceService;
import eu.wordpro.ha.server.service.dto.DeviceDTO;
import eu.wordpro.ha.server.service.impl.ThreadPoolsProvider;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MqttClient {

    private final Logger log = LoggerFactory.getLogger(MqttClient.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ThreadPoolsProvider threadPoolsProvider;

    @Value("${ha.mqtt.brokerUri}")
    private String brokerUrl;


    MqttAsyncClient client;

    @EventListener(ApplicationReadyEvent.class)
    private void setupConnection() throws MqttException {
        client = new MqttAsyncClient(brokerUrl, "HA_SERVER");
        threadPoolsProvider.getExecutorService().submit(() -> this.connect(client));
    }

    private void connect(MqttAsyncClient client){
        IMqttToken connectToken;
        try {
            connectToken = client.connect();
        } catch (MqttException e) {
            log.error("Error during mqtt connection", e);
            return;
        }
        try {
            connectToken.waitForCompletion(3000);
            log.info("Connected to mqtt broker");
        } catch (MqttException e) {
            log.warn("Timeout during waiting on connection completion. Scheduling next try in 30 seconds");
            threadPoolsProvider.getScheduledExecutorService().schedule(() -> connect(client), 30, TimeUnit.SECONDS);
            return;
        }

        try{
            subscribe(client);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void subscribe(MqttAsyncClient client) throws MqttException {
        List<DeviceDTO> allDevices = deviceService.findAll();
        log.info("Found {} devices", allDevices.size());
        for (DeviceDTO deviceDTO: allDevices){
            String topicName = deviceService.getMqttTopic(deviceDTO.getId()) + "/up";
            log.info("Subscribing to topic : {}", topicName);
            IMqttToken token = client.subscribe(topicName, 0, new DeviceMqttListener(deviceDTO, deviceService));
            token.waitForCompletion(10000);
            log.info("Subscribed to topic {}", topicName);
        }

    }

    public void sendData(byte[] toSend, String topic){
        threadPoolsProvider.getExecutorService().submit(() ->{
            try {
                IMqttDeliveryToken token = client.publish(topic, toSend, 2, false);
                token.waitForCompletion(10000);
                log.debug("Message send to topic");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });

    }


}
