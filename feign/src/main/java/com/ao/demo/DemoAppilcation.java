package com.ao.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages= {"com.ao.demo.feign"})  
@EnableEurekaClient
@SpringBootApplication
public class DemoAppilcation {
	public static void main(String[] args) {
		SpringApplication.run(DemoAppilcation.class, args);
	}
	
}
