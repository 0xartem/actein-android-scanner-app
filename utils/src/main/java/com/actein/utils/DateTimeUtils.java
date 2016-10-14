package com.actein.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils
{
    public static Date dateTimeMinus5Minutes(Date dateTime)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.add(Calendar.MINUTE, -5);
        return calendar.getTime();
    }

    public static long getDateDifference(Date olderDate, Date newerDate, TimeUnit timeUnit)
    {
        long millisecondsDiff = newerDate.getTime() - olderDate.getTime();
        return timeUnit.convert(millisecondsDiff, TimeUnit.MILLISECONDS);
    }
}
