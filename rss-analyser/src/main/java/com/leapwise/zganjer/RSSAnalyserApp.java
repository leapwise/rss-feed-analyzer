package com.leapwise.zganjer;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.leapwise")
public class RSSAnalyserApp {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		SpringApplication.run(RSSAnalyserApp.class, args);
	}

}
