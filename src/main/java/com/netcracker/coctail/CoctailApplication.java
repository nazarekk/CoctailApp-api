package com.netcracker.coctail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
public class CoctailApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoctailApplication.class, args);
	}

}
