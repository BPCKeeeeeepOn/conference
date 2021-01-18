package com.youyu.conference.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter yyyyMMddHHmmssSSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


    public static LocalDateTime localDateTimeToStr(String dateStr, DateTimeFormatter dateFormat) {
        return LocalDateTime.parse(dateStr, dateFormat);
    }

    public static LocalDateTime timestampToLocalDateTime(String timestamp) {
        if (StringUtils.isBlank(timestamp))
            return null;
        Instant instant = Instant.ofEpochMilli(Long.valueOf(timestamp));
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime timestampSecondToLocalDateTime(String timestamp) {
        if (StringUtils.isBlank(timestamp))
            return null;
        Instant instant = Instant.ofEpochSecond(Long.valueOf(timestamp));
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
