package com.google.zxing.client.android.result;

import android.app.Activity;

import com.google.zxing.client.android.R;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.ActeinCalendarParsedResult;
import com.google.zxing.client.result.ParsedResult;

import java.text.DateFormat;
import java.util.Date;

/**
 * Handles actein calendar entries encoded in QR Codes.
 *
 * @author Artem Brazhnikov
 */

public class ActeinCalendarResultHandler extends ResultHandler
{
    public ActeinCalendarResultHandler(Activity activity, ParsedResult result)
    {
        super(activity, result);
    }

    @Override
    public int getButtonCount()
    {
        return 0;
    }

    @Override
    public int getButtonText(int index)
    {
        return 0;
    }

    @Override
    public void handleButtonPress(int index)
    {
    }

    @Override
    public CharSequence getDisplayContents()
    {
        ActeinCalendarParsedResult acteinCalResult = (ActeinCalendarParsedResult) getResult();
        CalendarParsedResult calResult = acteinCalResult.getInnerCalendarResult();
        StringBuilder result = new StringBuilder(100);

        ParsedResult.maybeAppend(calResult.getSummary(), result);

        Date start = calResult.getStart();
        ParsedResult.maybeAppend(format(calResult.isStartAllDay(), start), result);

        Date end = calResult.getEnd();
        if (end != null) {
            if (calResult.isEndAllDay() && !start.equals(end)) {
                // Show only year/month/day
                // if it's all-day and this is the end date, it's exclusive, so show the user
                // that it ends on the day before to make more intuitive sense.
                // But don't do it if the event already (incorrectly?) specifies the same start/end
                end = new Date(end.getTime() - 24 * 60 * 60 * 1000);
            }
            ParsedResult.maybeAppend(format(calResult.isEndAllDay(), end), result);
        }

        ParsedResult.maybeAppend(calResult.getLocation(), result);
        ParsedResult.maybeAppend(calResult.getOrganizer(), result);
        ParsedResult.maybeAppend(calResult.getAttendees(), result);
        ParsedResult.maybeAppend(calResult.getDescription(), result);

        ParsedResult.maybeAppend(acteinCalResult.getEventType(), result);
        ParsedResult.maybeAppend(acteinCalResult.getGameName(), result);
        ParsedResult.maybeAppend(Integer.toString(acteinCalResult.getBoothId()), result);

        return result.toString();
    }

    @Override
    public int getDisplayTitle()
    {
        return R.string.actein_result_calendar;
    }

    private static String format(boolean allDay, Date date) {
        if (date == null) {
            return null;
        }
        DateFormat format = allDay
                ? DateFormat.getDateInstance(DateFormat.MEDIUM)
                : DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return format.format(date);
    }
}
