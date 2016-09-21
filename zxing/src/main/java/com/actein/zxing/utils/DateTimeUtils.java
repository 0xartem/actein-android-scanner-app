package com.actein.zxing.utils;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public static Date getCurrentInternetDateTime() {
        SntpClient sntpClient = new SntpClient();
        int timeout = 5000;
        if (sntpClient.requestTime("0.pool.ntp.org", timeout)) {
            long time = sntpClient.getNtpTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return calendar.getTime();
        }
        return new Date();
    }

    public static Date dateTimeMinus5Minutes(Date dateTime)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.add(Calendar.MINUTE, -5);
        return calendar.getTime();
    }
}
