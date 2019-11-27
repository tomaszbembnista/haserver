package eu.wordpro.ha.api;

public interface SignalProcessorData<T> {
    void setName(String name);
    String getName();
    T getData();
    byte[] toBytes();
}
