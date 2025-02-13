package com.Java24GroupProject.AirBnBPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class AirBnBPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirBnBPlatformApplication.class, args);
	}

}
