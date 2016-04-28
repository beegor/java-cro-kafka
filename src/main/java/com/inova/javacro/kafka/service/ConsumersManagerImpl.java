package com.inova.javacro.kafka.service;

import org.springframework.stereotype.Service;

import com.inova.javacro.kafka.core.JavaCroConsumer;
import com.inova.javacro.kafka.core.Topic;

import java.util.LinkedHashMap;
import java.util.Map;


@Service
public class ConsumersManagerImpl implements ConsumersManager {

    private Map<String, JavaCroConsumer> consumers = new LinkedHashMap<>();

    @Override
    public String addConsumer(Topic topic, String group) {
        String consumerId = System.currentTimeMillis() + "";
        consumers.put(consumerId, new JavaCroConsumer(consumerId, topic, group));
        return consumerId;
    }

    @Override
    public void destroyConsumer(String consumerId) {
        JavaCroConsumer consumer = consumers.remove(consumerId);
        if (consumer != null) {
            consumer.stop();
        }
    }

    @Override
    public Map<String, JavaCroConsumer> getConsumers() {
        return consumers;
    }
}
