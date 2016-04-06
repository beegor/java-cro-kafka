package com.inova.javacro.kafka.service;

import com.inova.javacro.kafka.core.JavacroProducer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProducersManager {


    private Map<String, JavacroProducer> producers = new ConcurrentHashMap<>();

    public String addProducer(String topic) {
        String producerId = UUID.randomUUID().toString();
        producers.put(producerId, new JavacroProducer(topic));
        return producerId;
    }

    public void destroyProducer(String producerId) {
        JavacroProducer producer = producers.remove(producerId);
        if (producer != null) {
            producer.stop();
        }
    }

    public Map<String, JavacroProducer> getProducers() {
        return producers;
    }
}
