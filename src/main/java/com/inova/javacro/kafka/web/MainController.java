package com.inova.javacro.kafka.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inova.javacro.kafka.service.ConsumersManager;
import com.inova.javacro.kafka.service.ProducersManager;
import com.inova.javacro.kafka.service.TopicManager;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


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
        model.addAttribute("speeds", getSpeedsMap());
        return "main";
    }


    @RequestMapping(value = "/state", produces = "application/json")
    @ResponseBody
    public Map<String, Map<String, Integer> >  getState() {
        Map<String, Map<String, Integer>> speeds = getSpeedsMap();
        return speeds;
    }



    private Map<String, Map<String, Integer>> getSpeedsMap() {

        Map<String, Map<String, Integer> > speeds = new HashMap<>();
        Map<String, Integer> producerSpeeds = producersManager.getProducers().entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getSpeedMsgPerSec()));

        Map<String, Integer> consumerSpeeds = consumersManager.getConsumers().entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getSpeedMsgPerSec()));

        speeds.put("producerSpeeds", producerSpeeds);
        speeds.put("consumerSpeeds", consumerSpeeds);
        return speeds;
    }
}
