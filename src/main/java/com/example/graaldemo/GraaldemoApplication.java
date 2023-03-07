package com.example.graaldemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GraaldemoApplication {

	@Autowired
	Listener listener;

	@RequestMapping("/")
	String home() {
		listener.send("Hello World!");
		return "completed send operation";
	}

	public static void main(String[] args) {
		SpringApplication.run(GraaldemoApplication.class, args);
	}

}
