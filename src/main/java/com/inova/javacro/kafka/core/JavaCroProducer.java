package com.inova.javacro.kafka.core;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.Future;

public class JavaCroProducer {

    private Properties config = new Properties();
    private static int msgNo = 1;
    private KafkaProducer producer;

    private Topic topic;

    private boolean active = false;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:SSS");

    public JavaCroProducer(String id, Topic topic) {
        this.topic = topic;
        config.put("client.id", id);
        config.put("bootstrap.servers", "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put("acks", "1");
        producer = new KafkaProducer<String, String>(config);

        new Thread(() -> {
            active = true;
            while (active) {
                sendMessage();
                Utils.sleep(300);
            }
        }).start();
    }


    private void sendMessage() {
        new Thread(() -> {
            String msg = "Poruka  " + LocalDateTime.now().format(dtf);
            Future<RecordMetadata> result = producer.send(new ProducerRecord(topic.getTopicName(), null, msg));
            try {
                RecordMetadata metadata = result.get();
                System.out.println("Sent msg: \"" + msg + "\"");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Msg failed: \"" + msg + "\"");
            }
        }).start();

    }


    public void stop() {
        active = false;
    }


    public Topic getTopic() {
        return topic;
    }
}