package com.inova.javacro.kafka.core;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class JavaCroProducer {

    private Properties config = new Properties();
    private KafkaProducer producer;

    private Topic topic;

    private int targetSpeed = 1500;
    private boolean active = false;
    private int speedMsgPerSec = 0;

    private final Logger log;

    private Map<String, Long> partitionOffsets = new ConcurrentHashMap<>();


    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:SSS");

    public JavaCroProducer(String id, Topic topic) {

        this.topic = topic;
        log = LoggerFactory.getLogger("PRODUCER-" + id);

        config.put("client.id", id);
        config.put("bootstrap.servers", "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put("acks", "1");
        producer = new KafkaProducer<String, String>(config);

        new Thread(() -> {

            active = true;
            while (active) {
                if (targetSpeed > 0) {
                    int msgPer10Milis = targetSpeed / 100;
                    for (int i = 0; i < msgPer10Milis; i++) {
                        sendMessage();
                        updateSpeed(1);
                    }
                }
                Utils.sleep(10);
            }
        }).start();
    }



    long msgNo = 0;
    private void sendMessage() {
        String timeStr = LocalDateTime.now().format(dtf);
        String msg = "Poruka  " + timeStr;
        producer.send(new ProducerRecord(topic.getTopicName(), timeStr, msg), msgSendListener);
    }


    public void stop() {
        active = false;
    }


    public Topic getTopic() {
        return topic;
    }


    public synchronized int getSpeedMsgPerSec() {
        int pastSecond = (int) ( System.currentTimeMillis() / 1000) - 1;
        int msgsInPastSecond =  speedPerSecond.containsKey(pastSecond) ? speedPerSecond.get(pastSecond) : 0;
        speedPerSecond.entrySet().removeIf( e -> e.getKey() < pastSecond);
        return msgsInPastSecond;
    }

    private Map<Integer, Integer> speedPerSecond = new LinkedHashMap<>();
    private void updateSpeed(int recordsCount) {
        int currentSecond = (int) (System.currentTimeMillis() / 1000);
        int currentSecondMsgCount = speedPerSecond.containsKey(currentSecond) ? speedPerSecond.get(currentSecond) : 0;
        currentSecondMsgCount += recordsCount;
        speedPerSecond.put(currentSecond, currentSecondMsgCount);
    }


    public void setTargetSpeed(int targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public Map<String, Long> getPartitionOffsets() {
        return partitionOffsets;
    }

    private Callback msgSendListener = new Callback() {

        private long lastLogTime = 0;

        @Override
        public void onCompletion(RecordMetadata data, Exception exception) {
            partitionOffsets.put(topic.getTopicName() + "_" + data.partition(), data.offset());
            if (System.currentTimeMillis() - lastLogTime > 1000) {
                log.debug("Offset for partition {} : {}", data.partition(), data.offset());
                lastLogTime = System.currentTimeMillis();

            }
        }
    };
}
