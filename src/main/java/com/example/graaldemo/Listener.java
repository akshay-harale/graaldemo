package com.example.graaldemo;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    @Autowired
    public AsyncTaskExecutor asyncTaskExecutor;

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);
    @SqsListener("sample-queue")
    public void receive(String message) throws InterruptedException {
        asyncTaskExecutor.submit(()-> {
            try {
                processData(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void processData(String message) throws InterruptedException {
        logger.info("Message: {}", message);
        Thread.sleep(2000);
        logger.info("Completed Message:{}", message);
    }
}
