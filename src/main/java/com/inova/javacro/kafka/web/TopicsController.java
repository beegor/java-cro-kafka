package com.inova.javacro.kafka.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inova.javacro.kafka.service.TopicManager;

@Controller
@RequestMapping("/topic")
public class TopicsController {

    @Autowired
    private TopicManager topicManager;


    @ResponseBody
    @RequestMapping("/add")
    public String addTopic(@RequestParam String topicName, @RequestParam int partitionCount) {

        try {
            topicManager.addTopic(topicName, partitionCount);
            return "Topic successfully added";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }


    @ResponseBody
    @RequestMapping("/delete")
    public String deleteTopic(@RequestParam String topicName) {

        try {
            topicManager.deleteTopic(topicName);
            return "Topic successfully deleted";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }


}
