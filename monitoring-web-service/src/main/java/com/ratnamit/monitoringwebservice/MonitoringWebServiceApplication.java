package com.ratnamit.monitoringwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;
/**
 * @author sumitmalpure1089
 *
 */
@EnableScheduling
@SpringBootApplication
@Slf4j
public class MonitoringWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringWebServiceApplication.class, args);
	}

}
