package com.inova.javacro.kafka;

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
    private final List<String> topics = new ArrayList<>();
    private final AtomicBoolean shutdown;
    private final CountDownLatch shutdownLatch;

    public JavaCroConsumer() {

        Properties config = new Properties();
        config.put("client.id", "localhost");
        config.put("group.id", "foo");
        config.put("bootstrap.servers", "localhost:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        topics.add("test");
        consumer = new KafkaConsumer(config);
        this.shutdown = new AtomicBoolean(false);
        this.shutdownLatch = new CountDownLatch(1);

        new Thread(this).start();
    }


    @Override
    public void run() {

        try {
            consumer.subscribe(topics);
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



    public static void main(String[] args) {
        new JavaCroConsumer();
    }


}
