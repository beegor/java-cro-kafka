package com.inova.javacro.kafka.service;

import com.inova.javacro.kafka.core.JavaCroConsumer;
import com.inova.javacro.kafka.core.Topic;

import java.util.Map;

public interface ConsumersManager {

    String addConsumer(Topic topic, String consumerGroup, Long msgProcDur);

    void destroyConsumer(String producerId);

    Map<String, JavaCroConsumer> getConsumers();
}
