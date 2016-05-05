package com.inova.javacro.kafka.core;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class JavaCroConsumer implements Runnable {


    private final KafkaConsumer<String, String> consumer;
    private final AtomicBoolean shutdown;
    private final CountDownLatch shutdownLatch;
    private final String group;
    private final Topic topic;
    private final String id;

    private final Logger log;

    private Map<String, Long> partitionOffsets = new HashMap<>();


    public JavaCroConsumer(String id, Topic topic, String group) {

        this.topic = topic;
        this.group = group;
        this.id = id;

        log = LoggerFactory.getLogger("CONSUMER-" + id);

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
                ConsumerRecords<String, String> records = consumer.poll(1000);

                updateSpeed(records);
                records.forEach(record -> process(record));
            }
        } finally {
            consumer.close();
            shutdownLatch.countDown();
        }
    }

    private void process(ConsumerRecord<String, String> record) {
//        System.out.println(record.value());
    }


    public void stop(){
        shutdown.set(true);
    }

    public Topic getTopic() {
        return topic;
    }

    public String getGroup() {
        return group;
    }

    public String getId() {
        return id;
    }





    public synchronized int getSpeedMsgPerSec() {
        int pastSecond = (int) ( System.currentTimeMillis() / 1000) - 1;
        int msgsInPastSecond =  speedPerSecond.containsKey(pastSecond) ? speedPerSecond.get(pastSecond) : 0;
        speedPerSecond.entrySet().removeIf( e -> e.getKey() < pastSecond);
        return msgsInPastSecond;
    }

    private Map<Integer, Integer> speedPerSecond = new LinkedHashMap<>();

    private void updateSpeed(ConsumerRecords<String, String> records) {

        int currentSecond = (int) (System.currentTimeMillis() / 1000);
        int currentSecondMsgCount = speedPerSecond.containsKey(currentSecond) ? speedPerSecond.get(currentSecond) : 0;
        currentSecondMsgCount += records.count();
        speedPerSecond.put(currentSecond, currentSecondMsgCount);

        records.partitions().forEach(p -> {
            int partition = p.partition();
            long offset= consumer.position(p);
            partitionOffsets.put(topic.getTopicName()+ "_" + group + "_" + partition, offset);
        });


    }
}
