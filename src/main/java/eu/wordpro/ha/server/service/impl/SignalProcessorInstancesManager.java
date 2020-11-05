package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.api.InvalidConfigurationException;
import eu.wordpro.ha.api.InvalidStateException;
import eu.wordpro.ha.api.StateListener;
import eu.wordpro.ha.server.domain.SignalProcessor;
import eu.wordpro.ha.server.repository.SignalProcessorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class SignalProcessorInstancesManager {

    private Map<Long, eu.wordpro.ha.api.SignalProcessor> instances = new HashMap<>();

    Logger logger = LoggerFactory.getLogger(SignalProcessorInstancesManager.class);

    @Autowired
    private SignalProcessorRepository signalProcessorRepository;


    public synchronized eu.wordpro.ha.api.SignalProcessor getInstance(SignalProcessor signalProcessorDescription){
        if (!instances.containsKey(signalProcessorDescription.getId())){
            eu.wordpro.ha.api.SignalProcessor result = instantiateSignalProcessor(signalProcessorDescription);
            if (initializeSignalProcessor(result, signalProcessorDescription)) {
                instances.put(signalProcessorDescription.getId(), result);
                updateStatus(signalProcessorDescription, "OK");
            }else{
                instances.put(signalProcessorDescription.getId(), null);
            }
        }


        return instances.get(signalProcessorDescription.getId());
    }

    private eu.wordpro.ha.api.SignalProcessor instantiateSignalProcessor(SignalProcessor signalProcessorDescription) {
        logger.info("Instantiating a signal processor: {}", signalProcessorDescription);
        String className = signalProcessorDescription.getClassName();
        Class<?> aClass;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.warn("Could not instantiate signal processor. Error: {}", e.getMessage());
            updateStatus(signalProcessorDescription, "Could not instantiate. Class not found");
            return null;
        }

        Object instance;
        try {
            instance = aClass.newInstance();
        } catch (Exception e) {
            logger.warn("Could not instantiate signal processor. Error: {}", e.getMessage());
            updateStatus(signalProcessorDescription, "Could not instantiate. " + e.getMessage());
            return null;
        }

        eu.wordpro.ha.api.SignalProcessor result;
        try {
            result = (eu.wordpro.ha.api.SignalProcessor) instance;
            result.setListener(new MyStateListener());
        }catch(ClassCastException e){
            logger.warn("Could not instantiate signal processor. Error: {}", e.getMessage());
            updateStatus(signalProcessorDescription, "Could not instantiate. " + e.getMessage());
            return null;
        }
        logger.info("Signal processor: {} instantiated successfully", signalProcessorDescription);
        return result;
    }

    private boolean initializeSignalProcessor(eu.wordpro.ha.api.SignalProcessor instance, SignalProcessor description){
        logger.info("Initializing a signal processor: {}", description);
        try {
            instance.setConfiguration(description.getConfiguration());
        } catch (InvalidConfigurationException e) {
            logger.warn("Could not initialize signal processor. Error during applying configuration: {}", e.getMessage());
            updateStatus(description, "Could not initialize. Error during applying configuration:" + e.getMessage());
            return false;
        }
        try {
            String state = description.getState();
            instance.setState(state);
        } catch (InvalidStateException e) {
            logger.warn("Could not initialize signal processor. Error during applying state: {}", e.getMessage());
            updateStatus(description, "Could not initialize. Error during applying state:" + e.getMessage());
            return false;
        }
        logger.info("Signal processor: {} initialized successfully", description);
        return true;

    }

    private SignalProcessor updateStatus(SignalProcessor toUpdate, String state){
        toUpdate.setStatus(state);
        return signalProcessorRepository.save(toUpdate);
    }

    @PreDestroy
    private void saveState(){
        for (Map.Entry<Long, eu.wordpro.ha.api.SignalProcessor> entry: instances.entrySet()){
            SignalProcessor signalProcessor = signalProcessorRepository.findById(entry.getKey()).orElse(null);
            if (signalProcessor == null){
                continue;
            }
            logger.info("Saving state of {}", entry);
            signalProcessor.setState(entry.getValue().getState());
            signalProcessorRepository.save(signalProcessor);
        }
    }

    private class MyStateListener implements StateListener{

        @Override
        public void onConfigurationChanged(String s) {

        }

        @Override
        public void onStateChanged(String s) {

        }
    }



}
