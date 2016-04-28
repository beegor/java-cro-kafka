package com.inova.javacro.kafka.core;

/**
 * Created by bigor on 04.04.16..
 */
public class Utils {


    public static  void sleep(long howLongMS) {
        try {
            Thread.sleep(howLongMS, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
