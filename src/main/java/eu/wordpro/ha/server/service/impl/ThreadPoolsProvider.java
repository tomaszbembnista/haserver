package eu.wordpro.ha.server.service.impl;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class ThreadPoolsProvider {

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

}
