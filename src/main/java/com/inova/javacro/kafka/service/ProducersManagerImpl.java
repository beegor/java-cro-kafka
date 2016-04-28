package com.inova.javacro.kafka.service;

import org.springframework.stereotype.Service;

import com.inova.javacro.kafka.core.JavaCroProducer;
import com.inova.javacro.kafka.core.Topic;

import java.util.LinkedHashMap;
import java.util.Map;


@Service
public class ProducersManagerImpl implements ProducersManager {

    private Map<String, JavaCroProducer> producers = new LinkedHashMap<>();

    @Override
    public String addProducer(Topic topic) {
        String producerId = System.currentTimeMillis() + "";
        producers.put(producerId, new JavaCroProducer(producerId, topic));
        return producerId;
    }

    @Override
    public void destroyProducer(String producerId) {
        JavaCroProducer producer = producers.remove(producerId);
        if (producer != null) {
            producer.stop();
        }
    }

    @Override
    public Map<String, JavaCroProducer> getProducers() {
        return producers;
    }
}
