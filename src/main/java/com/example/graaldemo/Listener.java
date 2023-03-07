package com.example.graaldemo;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessageChannel;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Component
@Service
public class Listener {

    @Autowired
    public AsyncTaskExecutor asyncTaskExecutor;

    @Autowired
    AmazonSQSAsync amazonSQSAsync;

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
    public void send(String message) {
        MessageChannel messageChannel = new QueueMessageChannel(amazonSQSAsync,"sample-queue");
        for (int i = 0; i < 7500; i++) {
            messageChannel.send(org.springframework.messaging.support.MessageBuilder.withPayload(message + " : "+ i).build());
        }
        logger.info("Completed send operation");
    }
}
