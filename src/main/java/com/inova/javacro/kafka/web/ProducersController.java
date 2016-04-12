package com.inova.javacro.kafka.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inova.javacro.kafka.core.Topic;
import com.inova.javacro.kafka.service.ProducersManager;
import com.inova.javacro.kafka.service.TopicManager;

@Controller
@RequestMapping("/producer")
public class ProducersController {

    @Autowired
    private ProducersManager producersManager;

    @Autowired
    private TopicManager topicManager;


    @ResponseBody
    @RequestMapping("/add")
    public String add(@RequestParam String topicName) {
        try {
            Topic topic = topicManager.getTopic(topicName);
            if (topic != null) {
                String producerId = producersManager.addProducer(topic);
                return "Producer successfully added";
            } else {
                return "ERROR: No such topic: " + topicName;
            }
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }


    @ResponseBody
    @RequestMapping("/delete")
    public String delete(@RequestParam String producerId) {

        try {
            producersManager.destroyProducer(producerId);
            return "Producer successfully deleted";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }


}
