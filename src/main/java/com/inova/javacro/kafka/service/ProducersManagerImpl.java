package com.inova.javacro.kafka.service;

import org.springframework.stereotype.Service;

import com.inova.javacro.kafka.core.JavaCroProducer;
import com.inova.javacro.kafka.core.Topic;

import javax.annotation.PreDestroy;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;


@Service
public class ProducersManagerImpl implements ProducersManager {

    private Map<String, JavaCroProducer> producers = new LinkedHashMap<>();

    private static int nextProducerId = 1;


    @Override
    public String addProducer(Topic topic) {
        String producerId = "" + nextProducerId++ ;
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


    @PreDestroy
    public void destroy(){
        for (JavaCroProducer producer : producers.values()) {
            producer.stop();
        }
    }
}
