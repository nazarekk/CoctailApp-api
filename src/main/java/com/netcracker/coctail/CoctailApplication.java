package com.netcracker.coctail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CoctailApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoctailApplication.class, args);
	}

}
