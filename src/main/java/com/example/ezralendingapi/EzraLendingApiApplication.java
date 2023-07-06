package com.example.ezralendingapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EzraLendingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzraLendingApiApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate){
//        return args ->{
//          kafkaTemplate.send("JOSEPHTOPIC", "DATA DATA");
//        };
//    }
}
