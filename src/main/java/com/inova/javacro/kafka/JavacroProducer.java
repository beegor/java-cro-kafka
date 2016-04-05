package com.inova.javacro.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Utils.sleep(300);
            System.out.println("Sent msg: \"" + msg + "\"");
        }
    }


    public static void main(String[] args) {
        new JavacroProducer();
    }
}
