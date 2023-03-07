# Getting Started

## SQS With localstack
This application demonstrate usage of sqs with localstack.
Install localstack
```
python3 -m pip install localstack
```
Start localstack with command (you should have docker on your machine)
```
localstack start
```
Above command will start container in docker. 
Check configuration in ```com.example.graaldemo.AppConfig.java```

Run class ```GraaldemoApplication.java``` from IDE 

Post sqs messages to locastack and you will see them get consumed by application.
Create Queue
```
awslocal sqs create-queue --queue-name sample-queue
```
and post message
```
awslocal sqs send-message --queue-url http://localhost:4566/00000000000/sample-queue --message-body test1
```
