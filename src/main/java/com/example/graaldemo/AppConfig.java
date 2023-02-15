package com.example.graaldemo;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean
    AmazonSQSAsync amazonSQS(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localhost:4566","us-east-1"))
                .withCredentials(
                        new AWSStaticCredentialsProvider(new BasicAWSCredentials("accessKey","secretKey")))
                .build();
    }

    @Bean(name = "sqs-thread-pool-executor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        asyncTaskExecutor.setMaxPoolSize(100);
        asyncTaskExecutor.setQueueCapacity(0);
        asyncTaskExecutor.setThreadNamePrefix("threadPoolExecutor-SQSContainer-");
        asyncTaskExecutor.initialize();
        asyncTaskExecutor.setRejectedExecutionHandler((r, executor) -> {
            BlockingQueue<Runnable> queue = executor.getQueue();
            try {
                if (!queue.offer(r, 3000l, TimeUnit.MILLISECONDS)) {
                    throw new RejectedExecutionException("Timeout");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        return asyncTaskExecutor;
    }

    @Bean
    public SimpleMessageListenerContainerFactory sqsContainerFactory() {
        SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory
                = new SimpleMessageListenerContainerFactory();
        simpleMessageListenerContainerFactory.setTaskExecutor(asyncTaskExecutor());
        simpleMessageListenerContainerFactory.setMaxNumberOfMessages(10);
        return simpleMessageListenerContainerFactory;
    }

}
