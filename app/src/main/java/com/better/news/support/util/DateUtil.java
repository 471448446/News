package com.better.news.support.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Better on 2016/4/5.
 */
public class DateUtil {
    /**
     * 用于知乎日报 获取前一天时间 20160318 前一天是20160317
     * @return
     */
    public static String getDateBeforDay(String date){
        DateFormat format=new SimpleDateFormat("yyyyMMdd");
        try {
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(format.parse(date));
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            return new StringBuffer().append(calendar.get(Calendar.YEAR)).
                    append(getDayStr(calendar.get(Calendar.MONTH)+1)).append(getDayStr(calendar.get(Calendar.DATE))).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String getDayStr(int day) {
        if(day<=9){
            return "0"+day;
        }else{
            return String.valueOf(day);
        }
    }
    public static String getTodayStr(){
        Calendar calendar=Calendar.getInstance();
        return ""+calendar.get(Calendar.YEAR)+getDayStr((calendar.get(Calendar.MONTH) + 1)) + getDayStr(calendar.get(Calendar.DATE));
    }

}
