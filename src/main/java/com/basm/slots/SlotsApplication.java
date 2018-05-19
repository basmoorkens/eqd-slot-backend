package com.basm.slots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.basm.slots"})
@EnableScheduling
public class SlotsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlotsApplication.class, args);
	}
}
