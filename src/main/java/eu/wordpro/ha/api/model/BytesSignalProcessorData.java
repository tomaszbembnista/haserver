package eu.wordpro.ha.api.model;

import eu.wordpro.ha.api.SignalProcessorData;

public class BytesSignalProcessorData implements SignalProcessorData<byte[]> {

    private String name;
    private final byte[] data;

    public BytesSignalProcessorData(byte[] data){
        this.data = data;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public byte[] toBytes() {
        return data;
    }
}
