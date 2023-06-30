package com.example.ezralendingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EzraLendingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzraLendingApiApplication.class, args);
    }

}
