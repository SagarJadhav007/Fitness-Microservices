package com.fitness.analyticsservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class AnalyticsserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsserviceApplication.class, args);
	}

}
