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

public class JavacroProducer {

    private Properties config = new Properties();
    private static int msgNo = 1;
    private KafkaProducer producer;

    private boolean active = false;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:SSS");

    public JavacroProducer(String topic) {

        config.put("client.id", "localhost");
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
        String msg = "Poruka  " + (msgNo++) + "  produced at " + LocalDateTime.now().format(dtf);
        Future<RecordMetadata> result = producer.send(new ProducerRecord("test", null, msg));
        try {
            RecordMetadata metadata = result.get();
            System.out.println("Sent msg: \"" + msg + "\"");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Msg failed: \"" + msg + "\"");
        }
    }


    public static void main(String[] args) {
        new JavacroProducer("test");
    }

    public void stop() {
        active = false;
    }
}
