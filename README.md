# Getting Started

## SQS With localstack
This application demonstrate usage of sqs with localstack.
Start localstack with command 
```
localstack start
```
Above command will start container in docker. 
Check configuration in ```com.example.graaldemo.AppConfig.java```

## GraalVM Native Support

This project has been configured to let you generate either a lightweight container or a native executable.
It is also possible to run your tests in a native image.

### Lightweight Container with Cloud Native Buildpacks
If you're already familiar with Spring Boot container images support, this is the easiest way to get started.
Docker should be installed and configured on your machine prior to creating the image.

To create the image, run the following goal:

```
$ ./gradlew bootBuildImage
```

Then, you can run the app like any other container:

```
$ docker run --rm -p 8080:8080 graaldemo:0.0.1-SNAPSHOT
```