package com.inova.javacro.kafka.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inova.javacro.kafka.core.Topic;
import com.inova.javacro.kafka.service.ConsumersManager;
import com.inova.javacro.kafka.service.TopicManager;

@Controller
@RequestMapping("/consumer")
public class ConsumersController {

    @Autowired
    private ConsumersManager consumersManager;

    @Autowired
    private TopicManager topicManager;


    @ResponseBody
    @RequestMapping("/add")
    public String add(@RequestParam String topicName, @RequestParam String group) {
        try {
            Topic topic = topicManager.getTopic(topicName);
            if (topic != null) {
                String consumerId = consumersManager.addConsumer(topic, group);
                return "Consumer successfully added: " + consumerId;
            } else {
                return "ERROR: No such topic: " + topicName;
            }
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }


    @ResponseBody
    @RequestMapping("/delete")
    public String delete(@RequestParam String consumerId) {

        try {
            consumersManager.destroyConsumer(consumerId);
            return "Consumer successfully deleted";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }


}
