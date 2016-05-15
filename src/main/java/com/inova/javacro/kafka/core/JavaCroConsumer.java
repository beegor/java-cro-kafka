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
import java.util.stream.Collectors;

public class JavaCroConsumer implements Runnable {


    private final KafkaConsumer<String, String> consumer;
    private final AtomicBoolean shutdown;
    private final CountDownLatch shutdownLatch;
    private final String group;
    private final String id;
    private Topic topic;

    private final Logger log;

    private Map<String, OffsetMark> partitionOffsets = new HashMap<>();

    // this is in micro seconds
    private long msgProcessDuration = 0;


    public JavaCroConsumer(String id, Topic topic, String group, Long msgProcessDuration) {

        this.topic = topic;
        this.group = group;
        this.id = id;
        this.msgProcessDuration = msgProcessDuration;

        log = LoggerFactory.getLogger("CONSUMER-" + id);

        Properties config = new Properties();
        config.put("client.id", id);
        config.put("group.id", group);
        config.put("auto.offset.reset", "earliest");
        config.put("bootstrap.servers", "localhost:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        consumer = new KafkaConsumer(config);
        consumer.subscribe( Arrays.asList(new String[]{topic.getTopicName()}));

        this.shutdown = new AtomicBoolean(false);
        this.shutdownLatch = new CountDownLatch(1);

        new Thread(this).start();
    }


    @Override
    public void run() {

        try {
            while (!shutdown.get()) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                records.forEach(record -> processMessage(record));
            }
        } finally {
            consumer.close();
            shutdownLatch.countDown();
        }
    }


    private void processMessage(ConsumerRecord<String, String> record) {

        partitionOffsets.put(record.topic() + "_" + record.partition(), new OffsetMark( System.currentTimeMillis(), record.offset()));

        if (msgProcessDuration > 0) {
            long milis = msgProcessDuration / 1000;
            int nanos = (int) (msgProcessDuration % 1000);
            Utils.sleep(milis, nanos);
        }
        updateSpeed();
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

    public long getMsgProcessDuration() {
        return msgProcessDuration;
    }

    public void setMsgProcessDuration(long msgProcessDuration) {
        this.msgProcessDuration = msgProcessDuration;
    }

    private Map<Integer, Integer> speedPerSecond = new LinkedHashMap<>();

    public synchronized int getSpeedMsgPerSec() {
        int pastSecond = (int) ( System.currentTimeMillis() / 1000) - 1;
        int msgsInPastSecond =  speedPerSecond.containsKey(pastSecond) ? speedPerSecond.get(pastSecond) : 0;
        speedPerSecond.entrySet().removeIf( e -> e.getKey() < pastSecond);
        return msgsInPastSecond;
    }

    private void updateSpeed() {

        int currentSecond = (int) (System.currentTimeMillis() / 1000);
        int currentSecondMsgCount = speedPerSecond.containsKey(currentSecond) ? speedPerSecond.get(currentSecond) : 0;
        currentSecondMsgCount += 1;
        speedPerSecond.put(currentSecond, currentSecondMsgCount);
    }

    public Map<String, Long> getPartitionOffsets() {
        partitionOffsets.entrySet().removeIf( e -> e.getValue().time < System.currentTimeMillis() - 500);
        return partitionOffsets.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(),
                e -> e.getValue().offset));
    }

    private class OffsetMark {
        private long time = 0;
        private long offset = 0;

        public OffsetMark(long time, long offset) {
            this.time = time;
            this.offset = offset;
        }
    }
}
