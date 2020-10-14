package com.interswitch.smartmoveserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    /*public static LocalDate toLocalDateFormat(Date startDate) {
        LocalDate localDateFormat = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDateFormat;
    }

    public static Date fromLocalDateFormat(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTimeFormat(Date startDate) {
        LocalDateTime localDateTimeFormat = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTimeFormat;
    }

    public static Date fromLocalDateTimeFormat(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date convertToDate(String dateString) {

        SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("MM:dd:yyyy");
        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date date = null;

        try {

            if (dateString.indexOf("-") > 0) {
                date = formatter3.parse(dateString);
            } else if (dateString.indexOf(":") > 0) {
                date = formatter2.parse(dateString);
            } else {
                date = formatter1.parse(dateString);
            }

        } catch (ParseException e) {
            logger.error(e);
        }

        return date;
    }

    public static String convertToText(Date date) {

        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ZoneId zoneId = ZoneId.of("Africa/Lagos");
        dateFormat.setTimeZone(TimeZone.getTimeZone(zoneId));
        return dateFormat.format(date);
    }*/

    public static LocalDateTime textToLocalDateTime(String text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm");
        LocalDateTime dateTime = LocalDateTime.parse(text, formatter);
        return dateTime;
    }

    public static String formatDate(LocalDateTime dateTime) {
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        DateFormat format = new SimpleDateFormat("MMM dd yyyy HH:mm aa");
        return format.format(date);
    }

    public static String formatDate(LocalDate date) {
        DateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
        return format.format(date);
    }

    public static String formatTime(LocalTime time) {
        DateFormat format = new SimpleDateFormat("HH:mm aa");
        return format.format(time);
    }

    public static String getTodayDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
/*
    public static String toYearMonth(Date dateString) {
        DateFormat formatter = new SimpleDateFormat("MMM yyyy");
        return formatter.format(dateString);
    }

    public static Date getDateInstance() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // Set time fields to zero
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // Put it back in the Date object
        date = cal.getTime();
        return date;
    }*/
}
