package com.inova.javacro.kafka.core;

public class Topic {

    private int partitionCount;

    private String topicName;

    private String topicColorHex;

    public Topic(int partitionCount, String topicName, String topicColorHex) {
        this.partitionCount = partitionCount;
        this.topicName = topicName;
        this.topicColorHex = topicColorHex;
    }

    public int getPartitionCount() {
        return partitionCount;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicColorHex() {
        return topicColorHex;
    }
}
