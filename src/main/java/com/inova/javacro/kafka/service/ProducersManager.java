package com.inova.javacro.kafka.service;

import com.inova.javacro.kafka.core.JavaCroProducer;
import com.inova.javacro.kafka.core.Topic;

import java.util.Map;

public interface ProducersManager {

    String addProducer(Topic topic);

    void destroyProducer(String producerId);

    Map<String, JavaCroProducer> getProducers();
}
