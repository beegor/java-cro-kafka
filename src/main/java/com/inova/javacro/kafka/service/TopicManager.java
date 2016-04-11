package com.inova.javacro.kafka.service;

import com.inova.javacro.kafka.core.Topic;

import java.util.List;

/**
 * Created by bigor on 08.04.16..
 */
public interface TopicManager {

    void addTopic(String topicValue, int partitionCount);

    List<Topic> getTopics();
}
