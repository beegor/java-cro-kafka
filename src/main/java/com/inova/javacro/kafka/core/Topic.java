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





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return topicName != null ? topicName.equals(topic.topicName) : topic.topicName == null;
    }

    @Override
    public int hashCode() {
        return topicName != null ? topicName.hashCode() : 0;
    }
}
