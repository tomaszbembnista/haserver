package eu.wordpro.ha.api.model;

import eu.wordpro.ha.api.SignalProcessorData;

import java.nio.charset.Charset;

public class StringSignalProcessorData implements SignalProcessorData<String> {
    private String name;
    private final String data;

    public StringSignalProcessorData(String data){
        this.data = data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    @Override
    public byte[] toBytes() {
        if (data != null){
            return data.getBytes(Charset.forName("UTF-8"));
        }
        return null;
    }


}
