package org.example;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public static Date parseDate(String dateStr) throws Exception {
        return sdf.parse(dateStr);
    }

    public static String formatDate(Date date) {
        return sdf.format(date);
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static boolean isAfter(Date date1, Date date2) {
        return date1.after(date2);
    }
}