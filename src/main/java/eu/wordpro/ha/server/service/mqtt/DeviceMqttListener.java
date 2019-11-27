package eu.wordpro.ha.server.service.mqtt;

import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.api.model.BytesSignalProcessorData;
import eu.wordpro.ha.server.service.DeviceService;
import eu.wordpro.ha.server.service.dto.DeviceDTO;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.LinkedList;

public class DeviceMqttListener implements IMqttMessageListener {

    private DeviceDTO device;
    private DeviceService deviceService;

    public DeviceMqttListener(DeviceDTO device, DeviceService deviceService){
        this.device = device;
        this.deviceService = deviceService;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        BytesSignalProcessorData rawData = new BytesSignalProcessorData(message.getPayload());
        rawData.setName("input");
        LinkedList<SignalProcessorData> input = new LinkedList<>();
        input.add(rawData);
        deviceService.executeProcessingChains(device.getId(), input);
    }

    public String getTopic(){
        return deviceService.getMqttTopic(this.device.getId());
    }
}
