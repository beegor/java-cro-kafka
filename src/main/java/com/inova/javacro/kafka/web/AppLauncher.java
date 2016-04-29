package com.inova.javacro.kafka.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.inova.javacro.kafka")
public class AppLauncher {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppLauncher.class, args);
    }


}
