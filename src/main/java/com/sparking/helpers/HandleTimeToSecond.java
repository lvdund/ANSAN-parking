package com.sparking.helpers;

import java.util.Arrays;

public class HandleTimeToSecond {
    public static double handleTimeToSecond(String time) {
        String[] getTimeRaw = time.split(":");
        int[] array = Arrays
                .stream(getTimeRaw)
                .mapToInt(Integer::parseInt)
                .toArray();

        int hour = array[0];
        int minute = array[1];
        int second = array[2];

        return hour * 3600 + minute * 60 + second;
    }
}
