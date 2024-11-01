package bsshelper.externalapi.perfmng.util;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeToString {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String nowTime() {
        return LocalDateTime.now().format(formatter);
    }

    public static String dayAgoTime() {
        return LocalDateTime.now().minusHours(24).format(formatter);
    }

    public static String weekAgoTime() {
        return LocalDateTime.now().minusDays(7).format(formatter);
    }

    public static String nHoursAgoTime(int hours) {
        return LocalDateTime.now().minusHours(hours).format(formatter);
    }
}
