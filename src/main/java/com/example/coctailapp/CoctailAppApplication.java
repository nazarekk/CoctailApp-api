package com.example.coctailapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
public class CoctailAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoctailAppApplication.class, args);
    }


}
