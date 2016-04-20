package com.inova.javacro.kafka.core;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class JavaCroConsumer implements Runnable {


    private final KafkaConsumer<String, String> consumer;
    private final AtomicBoolean shutdown;
    private final CountDownLatch shutdownLatch;

    private final String group;

    private final Topic topic;


    public JavaCroConsumer(String id, Topic topic, String group) {

        this.topic = topic;
        this.group = group;
        Properties config = new Properties();
        config.put("client.id", id);
        config.put("group.id", group);
        config.put("bootstrap.servers", "localhost:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        consumer = new KafkaConsumer(config);

        List<String> topics = new ArrayList<>();
        topics.add(topic.getTopicName());
        consumer.subscribe(topics);

        this.shutdown = new AtomicBoolean(false);
        this.shutdownLatch = new CountDownLatch(1);

        new Thread(this).start();
    }


    @Override
    public void run() {

        try {
            while (!shutdown.get()) {
                ConsumerRecords<String, String> records = consumer.poll(500);
                records.forEach(record -> process(record));
            }
        } finally {
            consumer.close();
            shutdownLatch.countDown();
        }
    }


    private void process(ConsumerRecord<String, String> record) {
        System.out.println(record.value());
    }

    public void stop() {
        shutdown.set(true);
    }

    public Topic getTopic() {
        return topic;
    }

    public String getGroup() {
        return group;
    }
}
