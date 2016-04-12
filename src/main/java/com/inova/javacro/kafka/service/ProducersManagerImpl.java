package com.inova.javacro.kafka.service;

import org.springframework.stereotype.Service;

import com.inova.javacro.kafka.core.JavacroProducer;
import com.inova.javacro.kafka.core.Topic;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class ProducersManagerImpl implements ProducersManager {

    private Map<String, JavacroProducer> producers = new ConcurrentHashMap<>();

    @Override
    public String addProducer(Topic topic) {
        String producerId = UUID.randomUUID().toString();
        producers.put(producerId, new JavacroProducer(topic));
        return producerId;
    }

    @Override
    public void destroyProducer(String producerId) {
        JavacroProducer producer = producers.remove(producerId);
        if (producer != null) {
            producer.stop();
        }
    }

    @Override
    public Map<String, JavacroProducer> getProducers() {
        return producers;
    }
}
