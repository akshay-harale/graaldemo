package com.example.graaldemo;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {
    @SqsListener("sample-queue")
    public void receive(String message) {
        System.out.println("Message: "+message);

    }
}
