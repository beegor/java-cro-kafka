package com.inova.javacro.kafka.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
@ComponentScan
public class MainController {


    @RequestMapping("/")
    public String showMainPage() {
        return "main";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainController.class, args);
    }

}
