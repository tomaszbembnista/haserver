package eu.wordpro.ha.server.service.mqtt;

import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.api.model.ProcessingData;
import eu.wordpro.ha.server.service.DeviceService;
import eu.wordpro.ha.server.service.dto.DeviceDTO;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class DeviceMqttListener implements IMqttMessageListener {

    private DeviceDTO device;
    private DeviceService deviceService;

    public DeviceMqttListener(DeviceDTO device, DeviceService deviceService){
        this.device = device;
        this.deviceService = deviceService;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        ProcessingData processingData = new ProcessingData();
        String inputValue = new String(message.getPayload(), Charset.forName("UTF-8"));
        SignalProcessorData inputData = new SignalProcessorData("input", inputValue);
        processingData.add(inputData);
        deviceService.processInputData(device.getId(), processingData);
    }

    public String getTopic(){
        return deviceService.getMqttTopic(this.device.getId());
    }
}
