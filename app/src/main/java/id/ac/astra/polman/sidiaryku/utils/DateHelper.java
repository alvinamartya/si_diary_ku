package id.ac.astra.polman.sidiaryku.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateHelper {
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate(String pattern) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            return now.format(DateTimeFormatter.ofPattern(pattern));
        } else {
            Date now = new Date();
            return new SimpleDateFormat(pattern).format(now);
        }
    }
}
