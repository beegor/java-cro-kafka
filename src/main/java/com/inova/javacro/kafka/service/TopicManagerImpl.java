package com.inova.javacro.kafka.service;

import kafka.admin.AdminUtils;
import kafka.api.TopicMetadata;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.springframework.stereotype.Service;
import scala.collection.Map;

import com.inova.javacro.kafka.core.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class TopicManagerImpl implements TopicManager {


    private ColorPicker colorPicker = new ColorPicker();

    private List<Topic> topics = new ArrayList<>();

    private static ZkUtils zkUtils;


    public TopicManagerImpl() {
        ZkConnection zkConnection = new ZkConnection("localhost:2181");
        ZkClient zkClient = new ZkClient(zkConnection);
        zkUtils = new ZkUtils(zkClient, zkConnection, false);

        Map<String, Properties> map = AdminUtils.fetchAllTopicConfigs(zkUtils);

        for (scala.collection.Iterator<String> iterator = map.keys().iterator(); iterator.hasNext(); ) {
            String topicValue = iterator.next();
            TopicMetadata tmd = AdminUtils.fetchTopicMetadataFromZk(topicValue, zkUtils);
            int partitonCount = tmd.partitionsMetadata().size();
            Topic topic = new Topic(partitonCount, topicValue, colorPicker.getNextColor());
            topics.add(topic);
        }
    }

    @Override
    public void addTopic(String topicValue, int partitionCount) {

        try {
            Topic topic = new Topic(partitionCount, topicValue, colorPicker.getNextColor());
            AdminUtils.createTopic(zkUtils, topicValue, partitionCount, 1, new Properties());
            topics.add(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Topic> getTopics() {
        return topics;
    }


    class ColorPicker {

        int currCol = 0;

        private String[] colors = new String[]{"#0000FF", "#8A2BE2", "#A52A2A", "#5F9EA0", "#D2691E", "#6495ED", "#006400", "#DAA520", "#008000", "#4B0082", "#FF0000", "#708090", "#9ACD32"};

        public synchronized String getNextColor() {

            String color = colors[currCol];
            currCol++;
            if (currCol >= colors.length) currCol = 0;
            return color;

        }

    }

}
