package com.inova.javacro.kafka.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inova.javacro.kafka.core.JavaCroConsumer;
import com.inova.javacro.kafka.core.JavaCroProducer;
import com.inova.javacro.kafka.core.Topic;
import com.inova.javacro.kafka.service.ConsumersManager;
import com.inova.javacro.kafka.service.ProducersManager;
import com.inova.javacro.kafka.service.TopicManager;

import java.util.*;
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
        Map<String, List<JavaCroConsumer>> consumers = new LinkedHashMap<>();
        consumersManager.getConsumers().entrySet().stream().forEach(entry -> {
            String groupName = entry.getValue().getTopic().getTopicName().toUpperCase() + "&nbsp;&nbsp;&nbsp;&nbsp;" + entry.getValue().getGroup().toUpperCase();
            List<JavaCroConsumer> groupConsumers = consumers.get(groupName);
            if (groupConsumers == null){
                groupConsumers = new ArrayList<>();
                consumers.put(groupName, groupConsumers);
            }
            groupConsumers.add(entry.getValue());
        });
        model.addAttribute("consumers", consumers);
        model.addAttribute("speeds", getSpeedsMap());
        model.addAttribute("partitionSizes", getPartitionSizes());
        return "main";
    }


    @RequestMapping(value = "/state", produces = "application/json")
    @ResponseBody
    public Map<String, Map<String, Integer>>  getState() {
        Map state = new HashMap<>();

        Map<String, Map<String, Integer>> speeds = getSpeedsMap();
        state.put("speeds", speeds);
        state.put("partitionSizes", getPartitionSizes());
        return state;
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


    private Map<String, Long> partitionSizes = new HashMap<>();

    public Map<String, Long> getPartitionSizes() {

        for (Topic topic : topicManager.getTopics()) {
            for (int partition = 0; partition < topic.getPartitionCount(); partition++) {
                String partitionKey = topic.getTopicName() + "_" + partition;

                long offset = partitionSizes.containsKey(partitionKey) ? partitionSizes.get(partitionKey) : 0;

                for (JavaCroProducer producer : producersManager.getProducers().values()) {
                    if (producer.getPartitionOffsets().containsKey(partitionKey)) {
                        Long producerOffset = producer.getPartitionOffsets().get(partitionKey);
                        if (producerOffset > offset) offset = producerOffset;
                    }
                }
                partitionSizes.put(partitionKey, offset);
            }
        }
        return partitionSizes;
    }
}
