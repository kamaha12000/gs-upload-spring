package com.elrouss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GsUploadingFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(GsUploadingFileApplication.class, args);
	}
}
