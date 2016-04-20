package com.inova.javacro.kafka.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inova.javacro.kafka.service.ConsumersManager;
import com.inova.javacro.kafka.service.ProducersManager;
import com.inova.javacro.kafka.service.TopicManager;


@Controller
@RequestMapping("/")
public class MainController {


    @Autowired
    private TopicManager topicManager;

    @Autowired
    private ProducersManager producersManager;

    @Autowired
    private ConsumersManager consumersManager;


    @RequestMapping("/")
    public String showMainPage(Model model) {
        model.addAttribute("topics", topicManager.getTopics());
        model.addAttribute("producers", producersManager.getProducers());
        model.addAttribute("consumers", consumersManager.getConsumers());
        return "main";
    }

    @RequestMapping("/state")
    public String getState(Model model) {
        model.addAttribute("topics", topicManager.getTopics());
        model.addAttribute("producers", producersManager.getProducers());
        return "main";
    }
}
