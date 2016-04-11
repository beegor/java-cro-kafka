package com.inova.javacro.kafka.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inova.javacro.kafka.service.TopicManager;

@Controller
public class MainController {

    @Autowired
    private TopicManager topicManager;

    @RequestMapping("/")
    public String showMainPage(Model model) {
        model.addAttribute("topics", topicManager.getTopics());
        return "main";
    }


    @RequestMapping("add-topic")
    public void addTopic(String topicName, int partitionCount) {
        topicManager.addTopic(topicName, partitionCount);
    }

}
