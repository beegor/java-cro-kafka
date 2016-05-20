# java-cro-kafka
Kafka 0.9.x producer / consumer example

Simple example of using Kafka 0.9.0.1 as message broker with producers and consumers implemented in Java.
It's a Spring Boot web application which demonstrates how to create topics, producers and consumers and displays current state of topic partitions, producers speed, consumers speed and offsets in real time.

#Prerequisites

1.Java 8 installed on your computer


#Installation

1. Download Kafka platform 2.0.1 from http://packages.confluent.io/archive/2.0/confluent-2.0.1-2.11.7.zip  and unzip it to location of your choice.

2. Clone this project , build it with gradle wrapper: from the project root run: "./gradlew build" This will produce javacro-kafka.jar located in {project_root}/build/libs


#Runing the demo

##Starting Kafka

1. Start Zookeeper - Open terminal and navigate to confluent home folder from step 1 of installation
run command "bin/zookeeper-server-start etc/kafka/zookeeper.properties"

2. Start Kafka broker - Open another terminal, navigate to confluent home folder and run command: "bin/kafka-server-start etc/kafka/server.properties"


##Staring demo app

1. In terminal, navigate to the cloned project root  and run:
    java -jar build/libs/javacro-kafka.jar

2. Open your browser (Google Chrome recommended) and go to url: http://localhost:9090


#Using the demo
First add some topics, then producers, and then consumers. Forms should be self explanatory.
Once you have some producers linked to topics, you can change their speed and observe how topic partitions get filled.
After you add some consumers you and link them to topics, you can observe consumer speed, and offset on partitions.

