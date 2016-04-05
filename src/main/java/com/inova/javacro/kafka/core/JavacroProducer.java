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

public class JavacroProducer implements Runnable{

    private Properties config = new Properties();
    private static int msgNo = 1;
    private KafkaProducer producer;



    public JavacroProducer() {
        config.put("client.id", "localhost");
        config.put("bootstrap.servers", "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put("acks", "1");
        producer = new KafkaProducer<String, String>(config);
        new Thread(this).start();
    }


    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:SSS");

    @Override
    public void run() {

        while (true) {

            String msg = "Poruka  " + (msgNo++) + "  produced at " + LocalDateTime.now().format(dtf) ;
            Future<RecordMetadata> result = producer.send(new ProducerRecord("test", null, msg));
            try {
                RecordMetadata metadata = result.get();
                System.out.println("Sent msg: \"" + msg + "\"");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Msg failed: \"" + msg + "\"");
            }
            Utils.sleep(300);

        }
    }


    public static void main(String[] args) {
        new JavacroProducer();
    }
}
