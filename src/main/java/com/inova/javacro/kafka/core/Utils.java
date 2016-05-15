package com.inova.javacro.kafka.core;


public class Utils {


    public static  void sleep(long howLongMS) {
        Utils.sleep(howLongMS, 0);
    }

    public static  void sleep(long howLongMS, int howLongNanos) {
        try {
            Thread.sleep(howLongMS, howLongNanos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
