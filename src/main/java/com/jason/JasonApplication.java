package com.jason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//(exclude = {SecurityAutoConfiguration.class})
public class JasonApplication {

	public static void main(String[] args) {
		SpringApplication.run(JasonApplication.class, args);
	}
}
