package com.inova.javacro.kafka.service;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import com.inova.javacro.kafka.core.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TopicManager {

    private List<Topic> topics = new ArrayList<>();

    private static ZkUtils zkUtils;

    static {
        ZkConnection zkConnection = new ZkConnection("localhost:2181");
        ZkClient zkClient = new ZkClient(zkConnection);
        zkUtils = new ZkUtils(zkClient, zkConnection, false);
    }


    public void addTopic(String topicValue, int partitionCount, String colorHex) {

        try {
            Topic topic = new Topic(partitionCount, topicValue, colorHex);
//        zkUtils, topic, partitions, replicas, configs, rackAwareMode
            AdminUtils.createTopic(zkUtils, topicValue, partitionCount, 1, new Properties());
            topics.add(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Topic> getTopics() {
        return topics;
    }

    public static void main(String[] args) {
        new TopicManager().addTopic("Jos_jedan_test", 3, "#ccaa99");
    }

}
