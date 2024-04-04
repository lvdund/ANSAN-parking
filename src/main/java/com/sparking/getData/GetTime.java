package com.sparking.getData;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class GetTime {
    public static Timestamp getTime(String time){
        int year = Integer.parseInt(time.substring(0,4));
//                        month in calendar of java must - 1
        int month = Integer.parseInt(time.substring(4,6)) -1 ;
        int day = Integer.parseInt(time.substring(6,8));
        int hour = Integer.parseInt(time.substring(8,10));
        int minute = Integer.parseInt(time.substring(10,12));
        int second = Integer.parseInt(time.substring(12,14));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        return new Timestamp(date.getTime());
    }
}
