package com.inova.javacro.kafka.core;

public class Topic {

    private int partitionCount;

    private String topicValue;

    private String topicColorHex;

    public Topic(int partitionCount, String topicValue, String topicColorHex) {
        this.partitionCount = partitionCount;
        this.topicValue = topicValue;
        this.topicColorHex = topicColorHex;
    }

    public int getPartitionCount() {
        return partitionCount;
    }

    public String getTopicValue() {
        return topicValue;
    }

    public String getTopicColorHex() {
        return topicColorHex;
    }
}
